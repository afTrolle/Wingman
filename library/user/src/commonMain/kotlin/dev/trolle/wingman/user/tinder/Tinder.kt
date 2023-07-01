@file:OptIn(ExperimentalSerializationApi::class)

package dev.trolle.wingman.user.tinder

import com.google.protobuf.StringValue
import dev.trolle.wingman.db.Database
import dev.trolle.wingman.user.tinder.model.MatchesResponse
import dev.trolle.wingman.user.tinder.model.MyProfileResponse
import dev.trolle.wingman.user.tinder.model.ProfileResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import io.ktor.serialization.kotlinx.protobuf.protobuf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.protobuf.ProtoBuf
import tinder.services.authgateway.AuthGatewayRequest
import tinder.services.authgateway.AuthGatewayResponse
import tinder.services.authgateway.EmailOtp
import tinder.services.authgateway.LoginResult
import tinder.services.authgateway.Phone
import tinder.services.authgateway.PhoneOtp
import tinder.services.authgateway.ValidateEmailOtpState
import tinder.services.authgateway.ValidatePhoneOtpState
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

interface Tinder {
    suspend fun matches(count: Int, pageToken: String?): MatchesResponse
    suspend fun profile(id: String): ProfileResponse
    suspend fun myProfile(): MyProfileResponse
    suspend fun isSignedIn(): Boolean
    suspend fun loginPhone(phoneNumber: String): AuthGatewayResponse
    suspend fun loginPhoneOtp(otpCode: String): AuthGatewayResponse
    suspend fun loginEmailOtp(otpCode: String): AuthGatewayResponse
}

internal fun tinder(
    protoBuf: ProtoBuf,
    json: Json,
    database: Database,
    rateLimiter: RateLimit = RateLimit(),
    host: String = "https://api.gotinder.com",
) = object : Tinder {

    val protoContentType = ContentType("application", "x-protobuf")
    private val session = TinderSession(database)

    private suspend fun onLoginResult(result: LoginResult) {
        val validFor = result.auth_token_ttl?.value?.milliseconds ?: 60.minutes
        val expires = Clock.System.now().plus(validFor)
        session.updateSession {
            it.copy(
                accessToken = SessionData.AccessToken(
                    token = result.auth_Token,
                    expireInstant = expires,
                ),
                refreshToken = result.refresh_token,
            )
        }
    }

    private suspend fun onValidatePhone(response: ValidatePhoneOtpState) = session.updateSession {
        val refresh = response.refresh_token?.value ?: it.refreshToken
        it.copy(phoneNumber = response.phone, refreshToken = refresh)
    }

    private suspend fun onValidateEmail(response: ValidateEmailOtpState) = session.updateSession {
        val refresh = response.refresh_token?.value ?: it.refreshToken
        it.copy(refreshToken = refresh)
    }

    private val client = HttpClient {
        // Throw exception on non-2xx responses
        expectSuccess = true
        install(ContentNegotiation) {
            json(json)
            @OptIn(ExperimentalSerializationApi::class)
            protobuf(protoBuf, protoContentType)
        }

        install(ContentEncoding) {
            gzip()
        }

        defaultRequest {
            headers {
                append(HttpHeaders.UserAgent, "Tinder Android Version 14.12.0")
                append("os-version", "27")
                append("platform", "android")
                append("platform-variant", "Google-Play")
                append("x-supported-image-formats", "webp")
                append("accept-language", "en-US")
                append("tinder-version", "14.12.0")
                append("store-variant", "Play-Store")
                append("persistent-device-id", session.persistenceId)
            }
        }

//        install(HttpRequestRetry) {
//            // only retry on network issues
//            retryOnExceptionIf(maxRetries = 3) { _, cause ->
//                when (cause) {
//                    is CancellationException -> false
//                    is IOException -> true
//                    else -> false
//                }
//            }
//            exponentialDelay()
//        }
    }

    /*
     Endpoint calls
     */
    override suspend fun loginPhone(phoneNumber: String): AuthGatewayResponse =
        authLoginCall(AuthGatewayRequest(phone = Phone(phone = phoneNumber)))

    override suspend fun loginPhoneOtp( otpCode: String): AuthGatewayResponse = session.withSession {
        val phoneNumber = it.phoneNumber ?: error("Phone number not set")
        authLoginCall(
            AuthGatewayRequest(
                phone_otp = PhoneOtp(
                    otp = otpCode,
                    phone = StringValue(phoneNumber),
                ),
            ),
        )
    }

    override suspend fun loginEmailOtp(otpCode: String): AuthGatewayResponse = session.withSession {
        val refreshToken = it.refreshToken ?: error("Missing refresh token")
        authLoginCall(
            AuthGatewayRequest(
                email_otp = EmailOtp(
                    otp = otpCode,
                    refresh_token = StringValue(refreshToken),
                ),
            ),
        )
    }

    override suspend fun matches(
        count: Int,
        pageToken: String?,
    ): MatchesResponse = session.withAccessToken { token ->
        commonDelayedRequest(
            token = token,
            path = "/v2/matches",
        ) {
            parameter("count", count)
            pageToken?.let { token ->
                parameter("page_token", token)
            }
        }
    }

    override suspend fun profile(id: String): ProfileResponse = session.withAccessToken { token ->
        commonDelayedRequest(
            token = token,
            path = "/user/$id",
        )
    }

    override suspend fun myProfile(): MyProfileResponse = session.withAccessToken { token ->
        commonDelayedRequest(
            token = token,
            path = "/profile",
        )
    }

    override suspend fun isSignedIn(): Boolean = session.isSignedIn()

    /*
     Utilities
     */

    private fun HttpRequestBuilder.commonRequestConfig(authToken: String, path: String) {
        header("x-auth-token", authToken)
        url { path(path) }
        contentType(ContentType.Application.Json)
    }

    private suspend inline fun <reified T> commonDelayedRequest(
        token: String,
        path: String,
        crossinline config: HttpRequestBuilder.() -> Unit = {},
    ): T {
        return withContext(Dispatchers.IO) {
            rateLimiter.delay()
            client.get(host) {
                commonRequestConfig(authToken = token, path = path)
                config()
            }.body()
        }
    }

    private suspend fun authLoginCall(request: AuthGatewayRequest): AuthGatewayResponse =
        withContext<AuthGatewayResponse>(Dispatchers.IO) {
            rateLimiter.delay()
            client.post(host) {
                url { path("/v3/auth/login") }
                contentType(protoContentType)
                setBody(request)
            }.body()
        }.apply {
            when {
                login_result != null -> onLoginResult(login_result)
                validate_phone_otp_state != null -> onValidatePhone(validate_phone_otp_state)
                validate_email_otp_state != null -> onValidateEmail(validate_email_otp_state)
            }
        }
}

object MissingDataException : Exception()

inline fun <T> T.applyIf(predicate: Boolean, action: (T) -> T): T =
    if (predicate) action(this) else this

@Serializable
data class SessionData(
    val accessToken: AccessToken? = null,
    val refreshToken: String? = null,
    val phoneNumber: String? = null,
) {

    @Serializable
    data class AccessToken(
        val token: String,
        val expireInstant: Instant,
    )
}


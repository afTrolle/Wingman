package dev.trolle.wingman.user.tinder

import dev.trolle.wingman.db.Database
import dev.trolle.wingman.user.tinder.model.AccessTokenResponse
import dev.trolle.wingman.user.tinder.model.ApiTokenRequest
import dev.trolle.wingman.user.tinder.model.MatchesResponse
import dev.trolle.wingman.user.tinder.model.MyProfileResponse
import dev.trolle.wingman.user.tinder.model.OtpRequest
import dev.trolle.wingman.user.tinder.model.ProfileResponse
import dev.trolle.wingman.user.tinder.model.RefreshApiTokenRequest
import dev.trolle.wingman.user.tinder.model.RefreshTokenResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

interface Tinder {
    suspend fun otp(phoneNumber: String)
    suspend fun refreshToken(oneTimePassword: String, phoneNumber: String): RefreshTokenResponse
    suspend fun matches(count: Int): MatchesResponse
    suspend fun profile(id: String): ProfileResponse
    suspend fun myProfile(): MyProfileResponse

    suspend fun isSignedIn(): Boolean
}

internal fun tinder(
    json: Json,
    database: Database,
    rateLimiter: RateLimit = RateLimit(),
    host: String = "https://api.gotinder.com",
) = object : Tinder {

    private val session = TinderSession(
        database,
    ) { accessToken(it) }

    private val client = HttpClient() {
        // Throw exception on non-2xx responses
        expectSuccess = true
        install(ContentNegotiation) {
            json(json)
        }
        install(HttpRequestRetry) {
            // only retry on network issues
            retryOnExceptionIf(maxRetries = 3) { _, cause ->
                when (cause) {
                    is CancellationException -> false
                    is IOException -> true
                    else -> false
                }
            }
            exponentialDelay()
        }
        install(UserAgent) {
            agent = "Tinder/11.4.0 (iPhone; iOS 12.4.1; Scale/2.00)"
        }
    }

    /*
     Endpoint calls
     */

    override suspend fun otp(phoneNumber: String) {
        rateLimiter.delay()
        client.post(host) {
            url { path("/v2/auth/sms/send") }
            contentType(ContentType.Application.Json)
            parameter("auth_type", "sms")
            setBody(OtpRequest(phoneNumber))
        }
    }

    override suspend fun refreshToken(
        oneTimePassword: String,
        phoneNumber: String,
    ): RefreshTokenResponse {
        rateLimiter.delay()
        return client.post(host) {
            url { path("/v2/auth/sms/validate") }
            contentType(ContentType.Application.Json)
            parameter("auth_type", "sms")
            setBody(RefreshApiTokenRequest(oneTimePassword, phoneNumber))
        }.body<RefreshTokenResponse>().also {
            val refreshToken = it.data.refreshToken
            val response = accessToken(refreshToken)
            val accessToken = response.data?.apiToken ?: error("Missing AccessToken")
            session.setSession(
                SessionData(
                    phoneNumber = phoneNumber,
                    refreshToken = response.data?.refreshToken ?: refreshToken,
                    accessToken = SessionData.AccessToken(accessToken),
                ),
            )
        }
    }

    private suspend fun accessToken(refreshToken: String): AccessTokenResponse {
        rateLimiter.delay()
        val result = client.post(host) {
            url { path("/v2/auth/login/sms") }
            contentType(ContentType.Application.Json)
            setBody(ApiTokenRequest(refreshToken))
        }
        return result.body()
    }

    override suspend fun matches(count: Int): MatchesResponse = session.withSession { token ->
        commonDelayedRequest(
            token = token,
            path = "/v2/matches",
        ) {
            parameter("count", count)
        }.body()
    }

    override suspend fun profile(id: String): ProfileResponse = session.withSession { token ->
        commonDelayedRequest(
            token = token,
            path = "/user/$id",
        ).body()
    }

    override suspend fun myProfile(): MyProfileResponse = session.withSession { token ->
        commonDelayedRequest(
            token = token,
            path = "/profile",
        ).body()
    }

    override suspend fun isSignedIn(): Boolean = session.isSignedIn()

    /*
     Utilities
     */

    private fun HttpRequestBuilder.commonRequestConfig(authToken: String, path: String) {
        header("X-Auth-Token", authToken)
        url { path(path) }
        contentType(ContentType.Application.Json)
    }

    private suspend fun commonDelayedRequest(
        token: String,
        path: String,
        config: HttpRequestBuilder.() -> Unit = {},
    ): HttpResponse {
        rateLimiter.delay()
        return client.get(host) {
            commonRequestConfig(authToken = token, path = path)
            config()
        }
    }
}

object MissingDataException : Exception()

@Serializable
data class SessionData(
    val accessToken: AccessToken? = null,
    val refreshToken: String,
    val phoneNumber: String,
) {

    @Serializable
    data class AccessToken(
        val token: String,
        val timestamp: Instant = Clock.System.now(),
    )
}


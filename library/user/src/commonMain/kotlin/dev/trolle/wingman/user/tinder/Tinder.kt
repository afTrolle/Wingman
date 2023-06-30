@file:OptIn(ExperimentalSerializationApi::class)

package dev.trolle.wingman.user.tinder

import dev.trolle.wingman.db.Database
import dev.trolle.wingman.user.tinder.model.AccessTokenResponse
import dev.trolle.wingman.user.tinder.model.MatchesResponse
import dev.trolle.wingman.user.tinder.model.MyProfileResponse
import dev.trolle.wingman.user.tinder.model.ProfileResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.UserAgent
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
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.CancellationException
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
import kotlin.random.Random

interface Tinder {

    suspend fun matches(count: Int, pageToken: String?): MatchesResponse
    suspend fun profile(id: String): ProfileResponse
    suspend fun myProfile(): MyProfileResponse
    suspend fun isSignedIn(): Boolean
    suspend fun login(authGatewayRequest: AuthGatewayRequest): AuthGatewayResponse
}


internal fun tinder(
    protoBuf: ProtoBuf,
    json: Json,
    database: Database,
    rateLimiter: RateLimit = RateLimit(),
    host: String = "https://api.gotinder.com",
) = object : Tinder {

    val protoContentType = ContentType("application", "x-protobuf")

    private val session = TinderSession(
        database,
    ) {
        AccessTokenResponse(null, null)
    }

    private val client = HttpClient() {
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

                // So one of these are needed
                val test = Random.nextLong().toString(16)
                // 64 bit value as hex - that needs to be stored.
                append("persistent-device-id", "66c87cc34a0dda82")
            }
        }

        // content-type: application/x-protobuf
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
    override suspend fun login(authGatewayRequest: AuthGatewayRequest): AuthGatewayResponse {
        return withContext(Dispatchers.IO) {
            rateLimiter.delay()
            client.post(host) {
                url { path("/v3/auth/login") }
                contentType(protoContentType)
                setBody(authGatewayRequest)
            }.body()
        }
    }

    override suspend fun matches(
        count: Int,
        pageToken: String?,
    ): MatchesResponse = session.withSession { token ->
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

    override suspend fun profile(id: String): ProfileResponse = session.withSession { token ->
        commonDelayedRequest(
            token = token,
            path = "/user/$id",
        )
    }

    override suspend fun myProfile(): MyProfileResponse = session.withSession { token ->
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


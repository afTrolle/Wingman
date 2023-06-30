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
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json


internal class TinderV3(
   val database: Database,
   val rateLimiter: RateLimit = RateLimit(),
   val host: String = "https://localhost",
) {
    val protoContentType = ContentType("application", "x-protobuf")

    private val client = HttpClient() {
        // Throw exception on non-2xx responses
        expectSuccess = true
        install(ContentNegotiation) {
            @OptIn(ExperimentalSerializationApi::class)
            protobuf(contentType = protoContentType)
        }

        install(ContentEncoding) {
            gzip()
        }

        defaultRequest {
            headers {
                append(HttpHeaders.UserAgent, "Tinder Android Version 14.12.0")
                append("os-version","27")
                append("app-version","4475")
                append( "platform",	"android")
                append("platform-variant","Google-Play")
                append("x-supported-image-formats","webp")
                append("accept-language","en-US")
                append("tinder-version","14.12.0")
                append("store-variant",	"Play-Store")
            }
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
    }

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
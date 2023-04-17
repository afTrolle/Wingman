package dev.trolle.af.wingman.service.tinder

import dev.trolle.af.wingman.service.tinder.model.AccessTokenResponse
import dev.trolle.af.wingman.service.tinder.model.ApiTokenRequest
import dev.trolle.af.wingman.service.tinder.model.MatchesResponse
import dev.trolle.af.wingman.service.tinder.model.MyProfileResponse
import dev.trolle.af.wingman.service.tinder.model.OtpRequest
import dev.trolle.af.wingman.service.tinder.model.ProfileResponse
import dev.trolle.af.wingman.service.tinder.model.RefreshApiTokenRequest
import dev.trolle.af.wingman.service.tinder.model.RefreshTokenResponse
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
import kotlinx.serialization.json.Json

interface TinderService {
    suspend fun otp(phoneNumber: String)
    suspend fun refreshToken(oneTimePassword: String, phoneNumber: String): RefreshTokenResponse
    suspend fun accessToken(refreshToken: String): AccessTokenResponse
    suspend fun matches(token: String, count: Int): MatchesResponse
    suspend fun profile(token: String, id: String): ProfileResponse
    suspend fun myProfile(token: String): MyProfileResponse
}

// expect val engine : HttpClientEngine

internal fun tinderService(
    json: Json,
    rateLimiter: RateLimit = RateLimit(),
    host: String = "https://api.gotinder.com"
) = object : TinderService {

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
        phoneNumber: String
    ): RefreshTokenResponse {
        rateLimiter.delay()
        return client.post(host) {
            url { path("/v2/auth/sms/validate") }
            contentType(ContentType.Application.Json)
            parameter("auth_type", "sms")
            setBody(RefreshApiTokenRequest(oneTimePassword, phoneNumber))
        }.body()
    }

    override suspend fun accessToken(refreshToken: String): AccessTokenResponse {
        rateLimiter.delay()
        val result = client.post(host) {
            url { path("/v2/auth/login/sms") }
            contentType(ContentType.Application.Json)
            setBody(ApiTokenRequest(refreshToken))
        }
        return result.body()
    }

    override suspend fun matches(token: String, count: Int): MatchesResponse =
        commonDelayedRequest(
            token = token,
            path = "/v2/matches"
        ) {
            parameter("count", count)
        }.body()

    override suspend fun profile(token: String, id: String): ProfileResponse =
        commonDelayedRequest(
            token = token,
            path = "/user/$id"
        ).body()

    override suspend fun myProfile(token: String): MyProfileResponse =
        client.get(host) {
            header("X-Auth-Token", token)
            url { path("/profile") }
            contentType(ContentType.Application.Json)
        }.body()

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
        config: HttpRequestBuilder.() -> Unit = {}
    ): HttpResponse {
        rateLimiter.delay()
        return client.get(host) {
            commonRequestConfig(authToken = token, path = path)
            config()
        }
    }
}

package com.poulastaa.common.network

import com.poulastaa.common.domain.Log
import com.poulastaa.common.domain.applyIf
import com.poulastaa.common.network.utils.NetworkPlatformUtils
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.plugins.timeout
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsBytes
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.encodedPath
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import kotlin.math.pow
import kotlin.random.Random

expect fun platformHttpClient(block: HttpClientConfig<*>.() -> Unit = {}): HttpClient

private const val RETRY_COUNT_KEY = "retry-count"
private const val MAX_RETRY_COUNT = 2
private const val MAX_REQUEST_TIMEOUT = 15_000L
private const val LOW_REQUEST_TIMEOUT = 10_000L
private const val MIN_REQUEST_TIMEOUT = 5_000L

private val RETRY_STATUS_CODE_LIST = listOf(
    // TODO: add status codes if needed
    502, 503, 504
)

// Base delay in milliseconds,
// for ex: if 2 with initialDelay 1s, then delays will be like 1s, 2s, 4s, 8s, etc.
private const val BASE_DELAY = 3f
private const val JITTER_RANDOMIZATION = 1_000L
private const val MAX_DELAY = 5_000L
private const val FIRST_DELAY = 1_000L
private const val NORMAL_DELAY = 500L

private val IGNORE_AUTH_TOKEN_ROUTES = listOf(
    // TODO: populate with actual routes
    "login",
    "refresh",
    "signup"
)

val isUserLoggedIn = false
val isRefreshNeeded = true
val refreshToken = ""
val accessToken: String? = null
val homeRoute = ""

class PlatformHttpClient : KoinComponent {
    companion object {
        val json by lazy {
            Json {
                isLenient = true
                prettyPrint = true
                explicitNulls = false
                ignoreUnknownKeys = true
                encodeDefaults = true
            }
        }
    }

    val client by lazy {
        platformHttpClient {
            expectSuccess = true

            install(HttpTimeout) {
                requestTimeoutMillis = MAX_REQUEST_TIMEOUT
                connectTimeoutMillis = MAX_REQUEST_TIMEOUT
                socketTimeoutMillis = MAX_REQUEST_TIMEOUT
            }

            install(ContentEncoding) {
                gzip(quality = 1.0f)
                deflate(quality = 0.9f)
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("HttpClient", message)
                    }
                }
                level = LogLevel.ALL
            }

            install(HttpRequestRetry) {
                delayMillis { count ->
                    val initialDelay =
                        if (response?.status?.value in RETRY_STATUS_CODE_LIST) FIRST_DELAY else NORMAL_DELAY

                    val delay = minOf(
                        (BASE_DELAY.pow(count - 1) + initialDelay).toLong(),
                        MAX_DELAY
                    )
                    delay + Random.nextLong(JITTER_RANDOMIZATION)
                }

                retryIf(MAX_RETRY_COUNT) { _, res ->
                    // Retry on specific gateway errors.
                    res.status.value in RETRY_STATUS_CODE_LIST
                }
            }

            install(DefaultRequest) {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                header(HttpHeaders.UserAgent, NetworkPlatformUtils.getPlatformUserAgent())
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        if (isUserLoggedIn) {
                            val accessToken = "" // return with null if not available
                            val refreshToken = "" // return with null if not available

                            BearerTokens(accessToken = accessToken, refreshToken = refreshToken)
                        } else null
                    }
//                    refreshTokens {
//                        val tokenResult = refreshToken()
//
//                        if (tokenResult == null) {
//                            logOut()
//                            throw IllegalStateException("Refresh token not available")
//                        }
//
//                        val accessToken = tokenResult.accessToken?.takeIf { it.isNotBlank() }
//                            ?: throw IllegalStateException("Access token not available")
//                        val refreshToken = tokenResult.refreshToken?.takeIf { it.isNotBlank() }
//                            ?: throw IllegalStateException("Refresh token not available")
//
//                        BearerTokens(
//                            accessToken = tokenResult.accessToken,
//                            refreshToken = tokenResult.refreshToken
//                        )
//                    }
                    sendWithoutRequest { request ->
                        // accessToken will be added except this routes
                        request.url.encodedPath !in IGNORE_AUTH_TOKEN_ROUTES
                    }
                }
            }
        }.also { client ->
            client.plugin(HttpSend).intercept { request ->
                val isRefreshRoute = request.url.toString().contains(
                    // TODO: add refresh route
                    "",
                    ignoreCase = true
                )
                if (isRefreshRoute.not()) refreshTokenIfNeeded()

                val isAuthNeeded = request.url.encodedPath !in IGNORE_AUTH_TOKEN_ROUTES
                if (isAuthNeeded && isUserLoggedIn && accessToken == null) {
                    logOut()
                    throw IllegalStateException("User logged in but, Access token not available")
                }

                // add fallback refresh token
                if (isRefreshRoute) {
                    // TODO: change with actual token
                    request.header(HttpHeaders.Authorization, "Bearer $refreshToken")
                }

                val retryCount = (request.headers[RETRY_COUNT_KEY]?.toIntOrNull() ?: 0)
                val originalCall = execute(
                    request.applyIf(retryCount > 0) { // if this request is already a retry attempt, use a shorter timeout
                        timeout { requestTimeoutMillis = LOW_REQUEST_TIMEOUT }
                    }.applyIf(
                        request.url.toString().contains(homeRoute)
                    ) {// if home route then must not wait for long i.e. hurts user experience
                        timeout { requestTimeoutMillis = MIN_REQUEST_TIMEOUT }
                    }
                )

                val response = originalCall.response
                val status = response.status

                if (isLogoutNeeded()) {
                    logOut()
                    throw IllegalStateException(
                        "User logged out due to server response: ${
                            response.bodyAsBytes().decodeToString()
                        } status: $status"
                    )
                } else originalCall
            }
        }
    }
}


private fun refreshTokenIfNeeded(): String? {
    // TODO: implement refresh token logic
    return ""
}

private fun refreshToken(): String? {
    // TODO: implement refresh token logic
    return ""
}

private fun logOut() {

}

private fun isLogoutNeeded(): Boolean {
    return false
}
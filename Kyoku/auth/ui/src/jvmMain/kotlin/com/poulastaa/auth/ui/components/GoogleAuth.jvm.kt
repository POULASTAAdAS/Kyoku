package com.poulastaa.auth.ui.components

import com.poulastaa.auth.domain.GoogleTokenExchange
import com.poulastaa.common.domain.Log
import com.poulastaa.common.domain.SharedConfig
import com.poulastaa.common.ui.utils.dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.koin.core.module.Module
import org.koin.dsl.module
import java.awt.Desktop
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.InetAddress
import java.net.ServerSocket
import java.net.URI
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

private const val TAG = "GoogleAuthJvm"
private const val AUTHORIZATION_ENDPOINT = "https://accounts.google.com/o/oauth2/v2/auth"
private const val CALLBACK_TIMEOUT_SECONDS = 30L
private val secureRandom = SecureRandom()

actual val googleAuthModule: Module = module {
    single<GoogleAuthWrapper> { JvmGoogleAuthWrapper(get()) }
}

private class JvmGoogleAuthWrapper(
    private val tokenExchange: GoogleTokenExchange,
) : GoogleAuthWrapper {
    private val scope = CoroutineScope(SupervisorJob() + dispatchers.io)
    private val mutex = Mutex()
    private var pendingResult: GoogleAuthResult? = null
    private var isRunning = false

    override var onResult: ((GoogleAuthResult) -> Unit)? = null

    override fun startGoogleAuth() {
        scope.launch {
            val started = mutex.withLock {
                if (isRunning) return@withLock false
                isRunning = true
                true
            }
            if (!started) return@launch

            Log.d(TAG, "Starting desktop Google auth flow")
            val clientId = SharedConfig.GOOGLE_JVM_CLIENT_ID
            val clientSecret = SharedConfig.GOOGLE_JVM_CLIENT_SECRET
            if (clientId.isBlank() || clientSecret.isBlank()) {
                Log.e(TAG, "Google auth failed: JVM client credentials are not configured")
                mutex.withLock { isRunning = false }
                publish(
                    GoogleAuthResult.Error(
                        IllegalStateException("Google client credentials are not configured")
                    )
                )
                return@launch
            }

            val result = runCatching {
                authenticate(clientId, clientSecret)
            }.getOrElse { exception ->
                Log.e(TAG, "Desktop Google auth failed", exception)
                GoogleAuthResult.Error(exception as? Exception ?: Exception(exception))
            }

            mutex.withLock { isRunning = false }
            publish(result)
        }
    }

    private suspend fun authenticate(
        clientId: String,
        clientSecret: String,
    ): GoogleAuthResult {
        val state = randomUrlSafeValue()
        val verifier = randomUrlSafeValue(64)
        val challenge = verifier.sha256Base64Url()

        return withContext(Dispatchers.IO) {
            ServerSocket(0, 1, InetAddress.getLoopbackAddress()).use { server ->
                server.soTimeout = (CALLBACK_TIMEOUT_SECONDS * 1_000).toInt()
                val redirectUri = "http://127.0.0.1:${server.localPort}/oauth/callback"
                val authorizationUri = buildAuthorizationUri(
                    clientId = clientId,
                    redirectUri = redirectUri,
                    state = state,
                    challenge = challenge,
                )

                if (!Desktop.isDesktopSupported() ||
                    !Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)
                ) return@withContext GoogleAuthResult.Error(IllegalStateException("System browser is not available"))

                Desktop.getDesktop().browse(URI(authorizationUri))
                val callback = readCallback(server)

                Log.d(TAG, "Received Google OAuth callback: $callback")

                if (callback["state"] != state) return@withContext GoogleAuthResult.Error(
                    IllegalStateException(
                        "Google OAuth state validation failed"
                    )
                )

                callback["error"]?.let { error ->
                    return@withContext if (error == "access_denied") GoogleAuthResult.Canceled
                    else GoogleAuthResult.Error(IllegalStateException("Google OAuth failed: $error"))
                }

                val code = callback["code"]
                    ?: return@withContext GoogleAuthResult.Error(IllegalStateException("Google OAuth code is missing"))

                return@withContext GoogleAuthResult.Success(
                    tokenExchange.exchange(
                        clientId = clientId,
                        clientSecret = clientSecret,
                        redirectUri = redirectUri,
                        code = code,
                        verifier = verifier,
                    )
                )
            }
        }
    }

    private fun readCallback(server: ServerSocket): Map<String, String> {
        val socket = server.accept()
        socket.use {
            val requestLine = BufferedReader(InputStreamReader(it.getInputStream())).readLine()
                ?: throw IllegalStateException("Google OAuth callback was empty")
            val target = requestLine.split(' ').getOrNull(1)
                ?: throw IllegalStateException("Google OAuth callback was malformed")
            val query = URI("http://127.0.0.1$target").rawQuery.orEmpty()

            val response =
                "HTTP/1.1 200 OK\r\nContent-Type: text/html; charset=utf-8\r\nConnection: close\r\n\r\n" +
                        "<html><body>You can return to Kyoku.</body></html>"
            it.getOutputStream().use { output ->
                output.write(response.toByteArray(StandardCharsets.UTF_8))
            }

            return query.split('&')
                .filter { q -> q.isNotBlank() }
                .associate { parameter ->
                    val parts = parameter.split('=', limit = 2)
                    URLDecoder.decode(parts[0], StandardCharsets.UTF_8) to
                            URLDecoder.decode(parts.getOrElse(1) { "" }, StandardCharsets.UTF_8)
                }
        }
    }

    private fun buildAuthorizationUri(
        clientId: String,
        redirectUri: String,
        state: String,
        challenge: String,
    ) = "$AUTHORIZATION_ENDPOINT?" + formUrlEncode(
        "client_id" to clientId,
        "code_challenge" to challenge,
        "code_challenge_method" to "S256",
        "redirect_uri" to redirectUri,
        "response_type" to "code",
        "scope" to "openid email profile",
        "state" to state,
    )

    private suspend fun publish(result: GoogleAuthResult) {
        val callback = mutex.withLock {
            Log.d(TAG, "Publishing Google auth result")
            onResult ?: run {
                pendingResult = result
                Log.d(TAG, "Google auth result queued until UI callback is available")
                return
            }
        }

        callback(result)
    }
}

private fun randomUrlSafeValue(size: Int = 32): String {
    val bytes = ByteArray(size)
    secureRandom.nextBytes(bytes)
    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
}

private fun String.sha256Base64Url() = Base64.getUrlEncoder().withoutPadding().encodeToString(
    MessageDigest.getInstance("SHA-256").digest(toByteArray(StandardCharsets.US_ASCII))
)

private fun formUrlEncode(
    vararg values: Pair<String, String>
) = values.joinToString("&") { (key, value) ->
    "${URLEncoder.encode(key, StandardCharsets.UTF_8)}=${
        URLEncoder.encode(
            value,
            StandardCharsets.UTF_8
        )
    }"
}

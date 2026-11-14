package com.poulastaa.auth.ui.components

import android.app.Activity
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.poulastaa.auth.domain.PasswordCredentialStore
import com.poulastaa.common.domain.Log
import com.poulastaa.common.domain.SharedConfig
import com.poulastaa.common.viewmodels.utils.dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.module.Module
import org.koin.dsl.module

private const val TAG = "GoogleAuth"

actual val googleAuthModule: Module = module {
    single(createdAtStart = true) { CurrentActivityTracker(get()) }
    single<GoogleAuthWrapper> { AndroidGoogleAuthWrapper(get()) }
    single<PasswordCredentialStore> { AndroidPasswordCredentialStore(get()) }
}

private class AndroidGoogleAuthWrapper(
    private val activityTracker: CurrentActivityTracker,
) : GoogleAuthWrapper {
    private val scope = CoroutineScope(SupervisorJob() + dispatchers.main)

    override var onResult: ((GoogleAuthResult) -> Unit)? = null

    override fun startGoogleAuth() {
        Log.d(TAG, "Starting Google auth flow")

        val activity = activityTracker.activity
        val clientId = SharedConfig.GOOGLE_MOBILE_CLIENT_ID

        if (activity == null) {
            Log.e(TAG, "Google auth failed: no active activity")
            onResult?.invoke(GoogleAuthResult.Canceled)
            return
        }

        if (clientId.isBlank()) {
            Log.e(TAG, "Google auth failed: mobile client id is not configured")
            onResult?.invoke(GoogleAuthResult.Canceled)
            return
        }

        scope.launch {
            val credentialManager = CredentialManager.create(activity)
            val token = credentialManager.getGoogleIdToken(activity, clientId)

            if (token == null) {
                Log.d(TAG, "Google auth canceled: no token returned")
                onResult?.invoke(GoogleAuthResult.Canceled)
            } else {
                Log.d(TAG, "Google auth completed with id token")
                onResult?.invoke(GoogleAuthResult.Success(token))
            }
        }
    }
}

private suspend fun CredentialManager.getGoogleIdToken(
    activity: Activity,
    clientId: String,
) = try {
    Log.d(TAG, "Requesting Google credential")

    val request = GetCredentialRequest.Builder()
        .addCredentialOption(
            GetGoogleIdOption.Builder()
                .setServerClientId(clientId)
                .setFilterByAuthorizedAccounts(false)
                .build()
        ).build()

    getCredential(context = activity, request = request).credential.toGoogleIdToken()
} catch (ex: NoCredentialException) {
    Log.d(TAG, "No saved Google credential found; falling back to explicit sign-in", ex)
    getGoogleIdTokenWithExplicitButton(activity, clientId)
} catch (ex: GetCredentialCancellationException) {
    Log.d(TAG, "Google credential request canceled", ex)
    null
} catch (ex: Exception) {
    Log.e(TAG, "Google credential request failed", ex)
    null
}

private suspend fun CredentialManager.getGoogleIdTokenWithExplicitButton(
    activity: Activity,
    clientId: String,
): String? = try {
    Log.d(TAG, "Requesting explicit Google sign-in")

    val request = GetCredentialRequest.Builder()
        .addCredentialOption(
            GetSignInWithGoogleOption.Builder(serverClientId = clientId).build()
        ).build()

    getCredential(context = activity, request = request).credential.toGoogleIdToken()
} catch (ex: GetCredentialCancellationException) {
    Log.d(TAG, "Explicit Google sign-in canceled", ex)
    null
} catch (ex: Exception) {
    Log.e(TAG, "Explicit Google sign-in failed", ex)
    null
}

private fun Credential.toGoogleIdToken(): String? {
    if (this !is CustomCredential || type != GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
        Log.e(TAG, "Google auth returned unsupported credential type")
        return null
    }

    return runCatching { GoogleIdTokenCredential.createFrom(data).idToken }
        .onFailure { Log.e(TAG, "Failed to parse Google id token credential", it) }
        .getOrNull()
}

package com.poulastaa.auth.ui.components

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.poulastaa.common.domain.Log
import com.poulastaa.common.domain.SharedConfig

private const val TAG = "GoogleAuth"

@Composable
actual fun StartActivityForResult(
    key: Boolean,
    onSuccess: (token: String) -> Unit,
    onCanceled: () -> Unit,
) {
    val context = LocalContext.current
    val clientId = SharedConfig.GOOGLE_WEB_CLIENT_ID

    LaunchedEffect(key, clientId) {
        if (key.not()) return@LaunchedEffect

        Log.d(TAG, "Starting Google auth flow")

        val activity = context.findActivity()
        if (activity == null || clientId.isBlank()) {
            Log.e(TAG, "Google auth canceled: activity missing or client id not configured")
            onCanceled()
            return@LaunchedEffect
        }

        val credentialManager = CredentialManager.create(activity)
        val token = credentialManager.getGoogleIdToken(activity, clientId)

        if (token == null) {
            Log.d(TAG, "Google auth canceled: no token returned")
            onCanceled()
        } else {
            Log.d(TAG, "Google auth completed with id token")
            onSuccess(token)
        }
    }
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
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

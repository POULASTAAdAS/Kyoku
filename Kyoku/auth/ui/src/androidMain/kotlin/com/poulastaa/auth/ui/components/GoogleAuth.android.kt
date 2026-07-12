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

private const val GOOGLE_WEB_CLIENT_ID_PLACEHOLDER = "TODO_GOOGLE_WEB_CLIENT_ID"

@Composable
actual fun StartActivityForResult(
    key: Boolean,
    clientId: String,
    onSuccess: (token: String) -> Unit,
    onCanceled: () -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(key, clientId) {
        if (key.not()) return@LaunchedEffect

        val activity = context.findActivity()
        if (activity == null || clientId.isBlank() || clientId == GOOGLE_WEB_CLIENT_ID_PLACEHOLDER) {
            onCanceled()
            return@LaunchedEffect
        }

        val credentialManager = CredentialManager.create(activity)
        val token = credentialManager.getGoogleIdToken(activity, clientId)

        if (token == null) onCanceled() else onSuccess(token)
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
): String? = try {
    val request = GetCredentialRequest.Builder()
        .addCredentialOption(
            GetGoogleIdOption.Builder()
                .setServerClientId(clientId)
                .setFilterByAuthorizedAccounts(false)
                .build()
        ).build()

    getCredential(context = activity, request = request).credential.toGoogleIdToken()
} catch (_: NoCredentialException) {
    getGoogleIdTokenWithExplicitButton(activity, clientId)
} catch (_: GetCredentialCancellationException) {
    null
} catch (_: Exception) {
    null
}

private suspend fun CredentialManager.getGoogleIdTokenWithExplicitButton(
    activity: Activity,
    clientId: String,
): String? = try {
    val request = GetCredentialRequest.Builder()
        .addCredentialOption(
            GetSignInWithGoogleOption.Builder(serverClientId = clientId).build()
        ).build()

    getCredential(context = activity, request = request).credential.toGoogleIdToken()
} catch (_: GetCredentialCancellationException) {
    null
} catch (_: Exception) {
    null
}

private fun Credential.toGoogleIdToken(): String? {
    if (this !is CustomCredential || type != GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
        return null
    }

    return runCatching { GoogleIdTokenCredential.createFrom(data).idToken }.getOrNull()
}

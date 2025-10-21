package com.poulastaa.auth.presentation.intro.components

import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.poulastaa.auth.presentation.BuildConfig
import com.poulastaa.core.domain.utils.JWTToken

@Composable
fun GoogleOneTapOnResult(
    key: Boolean,
    onSuccess: (token: JWTToken) -> Unit,
    onCanceled: () -> Unit,
) {
    val activity = LocalActivity.current ?: return

    LaunchedEffect(key) {
        if (key) {
            try {
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(BuildConfig.CLIENT_ID)
                    .setAutoSelectEnabled(true)
                    .build()

                val req = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val result = CredentialManager.create(activity).getCredential(
                    context = activity,
                    request = req
                )

                handleSignIn(result, onSuccess = onSuccess, onCanceled = onCanceled)
            } catch (e: GetCredentialCancellationException) {
                Log.e("google: GetCredentialCancellationException", "User cancelled: ${e.message}")
                onCanceled()
            } catch (e: GetCredentialException) {
                Log.e(
                    "google: GetCredentialException",
                    "Credential error: ${e.type} - ${e.message}"
                )
                onCanceled()
            } catch (e: Exception) {
                Log.e(
                    "google: Exception",
                    "Unknown error: ${e.javaClass.simpleName} - ${e.message}"
                )
                onCanceled()
            }
        }
    }
}

fun handleSignIn(
    result: GetCredentialResponse,
    onSuccess: (token: String) -> Unit,
    onCanceled: () -> Unit,
) {
    when (val credential = result.credential) {
        is CustomCredential -> {
            if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                try {
                    val googleIdTokenCredential = GoogleIdTokenCredential
                        .createFrom(credential.data)

                    onSuccess.invoke(googleIdTokenCredential.idToken)
                } catch (e: GoogleIdTokenParsingException) {
                    Log.e("google: handleSignIn", e.message.toString())
                    onCanceled.invoke()
                }
            } else onCanceled.invoke()
        }

        else -> onCanceled.invoke()
    }
}
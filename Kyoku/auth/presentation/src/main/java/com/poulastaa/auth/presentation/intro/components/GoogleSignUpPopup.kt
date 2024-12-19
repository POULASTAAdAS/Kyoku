package com.poulastaa.auth.presentation.intro.components

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException

@Composable
fun StartActivityForResult(
    key: Boolean,
    activity: Activity,
    clientId: String,
    onSuccess: (token: String) -> Unit,
    onCanceled: () -> Unit,
) {
    LaunchedEffect(key1 = key) {
        if (key) {
            try {
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(clientId)
                    .setAutoSelectEnabled(true)
                    .build()

                val req = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val result = CredentialManager.create(activity).getCredential(
                    context = activity,
                    request = req,
                )

                handleSignIn(
                    result = result,
                    onSuccess = onSuccess,
                    onCanceled = onCanceled
                )
            }catch (_: Exception) {
                onCanceled.invoke()
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
                } catch (_: GoogleIdTokenParsingException) {
                    onCanceled.invoke()
                }
            } else onCanceled.invoke()
        }

        else -> onCanceled.invoke()
    }
}
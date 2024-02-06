package com.poulastaa.kyoku.presentation.screen.auth.root.google

import android.app.Activity
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.poulastaa.kyoku.BuildConfig

@Composable
fun StartActivityForResult(
    key: Any,
    onResultReceived: (String) -> Unit,
    onDialogDismissed: () -> Unit,
    launcher: (ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>) -> Unit
) {
    val activity = LocalContext.current as Activity

    val activityLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult()) {
            try {
                if (it.resultCode == Activity.RESULT_OK) {
                    val oneTapClient = Identity.getSignInClient(activity)
                    val credential = oneTapClient.getSignInCredentialFromIntent(it.data)

                    val tokenId = credential.googleIdToken

                    if (tokenId != null) onResultReceived(tokenId)
                    else onDialogDismissed()
                }
            } catch (e: ApiException) {
                onDialogDismissed()
            }
        }

    LaunchedEffect(key1 = key) {
        launcher(activityLauncher)
    }
}


fun singIn(
    activity: Activity,
    launchActivityResult: (IntentSenderRequest) -> Unit,
    accountNotFound: () -> Unit,
    somethingWentWrong: () -> Unit
) {
    val oneTapClient = Identity.getSignInClient(activity)

    val signInRequest = BeginSignInRequest
        .builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest
                .GoogleIdTokenRequestOptions
                .builder()
                .setSupported(true)
                .setServerClientId(BuildConfig.CLIENT_ID)
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        .setAutoSelectEnabled(true)
        .build()

    oneTapClient
        .beginSignIn(signInRequest)
        .addOnSuccessListener {
            try {
                launchActivityResult(
                    IntentSenderRequest.Builder(
                        it.pendingIntent.intentSender
                    ).build()
                )
            } catch (e: Exception) {
                somethingWentWrong.invoke()
            }
        }
        .addOnFailureListener {
            signUp(
                activity = activity,
                launchActivityResult = launchActivityResult,
                accountNotFound = accountNotFound,
                somethingWentWrong = somethingWentWrong
            )
        }
}

fun signUp(
    activity: Activity,
    launchActivityResult: (IntentSenderRequest) -> Unit,
    accountNotFound: () -> Unit,
    somethingWentWrong: () -> Unit
) {
    val oneTapClient = Identity.getSignInClient(activity)
    val signInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(BuildConfig.CLIENT_ID)
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        .build()

    oneTapClient.beginSignIn(signInRequest)
        .addOnSuccessListener { result ->
            try {
                launchActivityResult(
                    IntentSenderRequest.Builder(
                        result.pendingIntent.intentSender
                    ).build()
                )
            } catch (e: Exception) {
                somethingWentWrong.invoke()
            }
        }
        .addOnFailureListener {
            accountNotFound()
        }
}
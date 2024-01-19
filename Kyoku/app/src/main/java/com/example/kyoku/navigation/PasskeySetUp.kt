package com.example.kyoku.navigation

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Base64
import android.util.Log
import androidx.credentials.CreateCredentialResponse
import androidx.credentials.CreatePublicKeyCredentialRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetPasswordOption
import androidx.credentials.GetPublicKeyCredentialOption
import androidx.credentials.exceptions.CreateCredentialCancellationException
import androidx.credentials.exceptions.CreateCredentialCustomException
import androidx.credentials.exceptions.CreateCredentialException
import androidx.credentials.exceptions.CreateCredentialInterruptedException
import androidx.credentials.exceptions.CreateCredentialProviderConfigurationException
import androidx.credentials.exceptions.CreateCredentialUnknownException
import androidx.credentials.exceptions.publickeycredential.CreatePublicKeyCredentialDomException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.SecureRandom
import java.util.UUID

fun signIn(
    context: Activity
) {
    val credentialManager = CredentialManager.create(context)

    val getPasswordOption = GetPasswordOption()

    // Get passkey from the user's public key credential provider.
    val getPublicKeyCredentialOption = GetPublicKeyCredentialOption(
        requestJson = ""
    )

    val getCredRequest = GetCredentialRequest(
        listOf(getPasswordOption, getPublicKeyCredentialOption)
    )

    val scope = CoroutineScope(Dispatchers.IO)

    scope.launch {
        try {
            val result = credentialManager.getCredential(
                context = context,
                request = getCredRequest
            )

            Log.d("credential", result.credential.toString())
            Log.d("data", result.credential.data.toString())
        } catch (e: Exception) {
            Log.d("error", e.message.toString())
        }
    }
}

fun signUp(
    activity: Activity,
    reqJson: String
) {
    val credentialManager = CredentialManager.create(activity)

    val createPublicKeyCredentialRequest = CreatePublicKeyCredentialRequest(
        requestJson = reqJson,
    )

    val scope = CoroutineScope(Dispatchers.IO)


    scope.launch {
        try {
            val result = credentialManager.createCredential(
                // Use an activity-based context to avoid undefined system
                // UI launching behavior
                context = activity,
                request = createPublicKeyCredentialRequest,
            )
            handlePasskeyRegistrationResult(result)
        } catch (e: CreateCredentialException) {
            handleFailure(e)
        }
    }
}

fun handleFailure(e: CreateCredentialException) {
    when (e) {
        is CreatePublicKeyCredentialDomException -> {
            // Handle the passkey DOM errors thrown according to the
            // WebAuthn spec.
            Log.e("CreatePublicKeyCredentialDomException", e.message.toString())
        }

        is CreateCredentialCancellationException -> {
            // The user intentionally canceled the operation and chose not
            // to register the credential.
            Log.e("CreateCredentialCancellationException", e.message.toString())
        }

        is CreateCredentialInterruptedException -> {
            // Retry-able error. Consider retrying the call.
            Log.e("CreateCredentialInterruptedException", e.message.toString())
        }

        is CreateCredentialProviderConfigurationException -> {
            // Your app is missing the provider configuration dependency.
            // Most likely, you're missing the
            // "credentials-play-services-auth" module.
            Log.e("CreateCredentialProviderConfigurationException", e.message.toString())
        }

        is CreateCredentialUnknownException -> {
            Log.e("CreateCredentialUnknownException", e.message.toString())
        }

        is CreateCredentialCustomException -> {
            Log.e("CreateCredentialCustomException", e.message.toString())
        }

        else -> Log.e("Unexpected", "Unexpected exception type ${e.message}")
    }
}

//


//


//


//

fun createPasskey(
    requestJson: String,
    activity: Activity,
) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val result = CredentialManager.create(activity).createCredential(
                context = activity,
                request = CreatePublicKeyCredentialRequest(
                    requestJson = requestJson,
                ),
            )
            handlePasskeyRegistrationResult(result)
        } catch (e: CreateCredentialException) {
            e.printStackTrace()
            Log.d("CreateCredentialException", e.message.toString())
        }
    }
}

private fun handlePasskeyRegistrationResult(response: CreateCredentialResponse) {
    Log.d("data", response.toString())
}

val req =
    """{"challenge":"0ITok9ajW6-ELyZvdqEe5uUDZk4bvqFQN4aDqgDazAwiet5wHCpyGt_4rN3dbrsGAV7FgI5uhfEY88UgsoRJJQ","rp":{"name":"Kyoku","id":"ccba-103-44-173-204.ngrok-free.app"},"user":{"id":${UUID.randomUUID()},"name":"poulastaadas2@gmail.com","displayName":"Anshu"},"pubKeyCredParams":[{"type":"public-key","alg":-7}],"timeout":1800000,"attestation":"none","excludeCredentials":[],"authenticatorSelection":{"authenticatorAttachment":"platform","requireResidentKey":false,"residentKey":"required","userVerification":"required"}}"""


fun generateFidoChallenge(): String {
    val secureRandom = SecureRandom()
    val challengeBytes = ByteArray(64)
    secureRandom.nextBytes(challengeBytes)
    return challengeBytes.b64Encode()
}

fun ByteArray.b64Encode(): String {
    return Base64.encodeToString(this, Base64.URL_SAFE)
}
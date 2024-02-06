package com.poulastaa.kyoku.presentation.screen.auth.root.passkey

import android.app.Activity
import android.util.Log
import androidx.credentials.CreatePublicKeyCredentialRequest
import androidx.credentials.CreatePublicKeyCredentialResponse
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetPublicKeyCredentialOption
import androidx.credentials.PublicKeyCredential
import androidx.lifecycle.viewModelScope
import com.poulastaa.kyoku.BuildConfig
import com.poulastaa.kyoku.data.model.api.auth.passkey.CreatePublicKeyCredential
import com.poulastaa.kyoku.data.model.api.auth.passkey.GetPublicKeyCredential
import com.poulastaa.kyoku.data.model.api.auth.passkey.PasskeyClientData
import com.poulastaa.kyoku.presentation.screen.auth.root.RootAuthViewModel
import com.poulastaa.kyoku.utils.b64Decode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.nio.charset.StandardCharsets

suspend fun createPasskey(
    credentialManager: CredentialManager,
    jsonString: String,
    activity: Activity,
    challenge: String,
    sendUserToServer: (id: String?) -> Unit
) {
    val result = credentialManager.createCredential(
        context = activity,
        request = CreatePublicKeyCredentialRequest(
            requestJson = jsonString
        )
    )

    val json =
        (result as CreatePublicKeyCredentialResponse).registrationResponseJson

    Log.d("json", json)

    val publicKeyCredentialResponse =
        Json.decodeFromString<CreatePublicKeyCredential>(json)

    val status = toPasskeyClientData( // get user data
        publicKeyCredentialResponse
            .response.clientDataJSON
    ).validate(challenge) // validate user data

    if (status) sendUserToServer.invoke(publicKeyCredentialResponse.id) // send user id back
    else sendUserToServer.invoke(null)
}

fun RootAuthViewModel.getPasskey(
    credentialManager: CredentialManager,
    jsonString: String,
    activity: Activity,
    challenge: String,
    getUserFromId: (id: String?) -> Unit
) {
    viewModelScope.launch(Dispatchers.IO) {
        val option = GetPublicKeyCredentialOption(
            requestJson = jsonString
        )

        val credentialRequest = GetCredentialRequest(
            credentialOptions = listOf(option),
        )

        val response = credentialManager.getCredential(
            context = activity,
            request = credentialRequest
        ).credential as PublicKeyCredential

        Log.d("dataaaaaaaaaaaaaaaa", response.authenticationResponseJson)

        val getPublicKeyCredential =
            Json.decodeFromString<GetPublicKeyCredential>(response.authenticationResponseJson)

        val status = toPasskeyClientData(getPublicKeyCredential.response.clientDataJSON)
            .validate(challenge)

        if (status) getUserFromId.invoke(getPublicKeyCredential.id)
        else getUserFromId.invoke(null)
    }
}

private fun toPasskeyClientData(clientDataJson: String): PasskeyClientData {
    val byteArray = clientDataJson.b64Decode()
    val json = String(byteArray, StandardCharsets.UTF_8)
    return Json.decodeFromString<PasskeyClientData>(json)
}

private fun PasskeyClientData.validate(
    challenge: String
): Boolean = this.challenge == challenge && this.origin == BuildConfig.ORIGIN
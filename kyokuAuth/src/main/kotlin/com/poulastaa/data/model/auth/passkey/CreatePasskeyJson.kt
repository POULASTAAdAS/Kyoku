package com.poulastaa.data.model.auth.passkey

import com.poulastaa.utils.Constants.AUTH_RESPONSE_PASSKEY_TYPE_SIGN_UP
import com.poulastaa.utils.generateFidoChallenge
import kotlinx.serialization.Serializable

@Serializable
data class CreatePasskeyJson(
    val challenge: String = generateFidoChallenge(),
    val rp: Rp = Rp(),
    val user: User = User(),
    val pubKeyCredParams: List<PubKeyCredParams> = listOf(
        PubKeyCredParams()
    ),
    val timeout: Long = 1800000,
    val attestation: String = "none",
    val excludeCredentials: List<ExcludeCredentials> = emptyList(),
    val authenticatorSelection: AuthenticatorSelection = AuthenticatorSelection()
): PasskeyBaseModel(type = AUTH_RESPONSE_PASSKEY_TYPE_SIGN_UP) {

    @Serializable
    data class Rp(
        val name: String = "Kyoku",
        val id: String = "com.example.kyoku"
    )

    @Serializable
    data class User(
        val id: String = "1",
        val name: String = "poulastaadas2@gmail.com",
        val displayName: String = "Anshu"
    )

    @Serializable
    data class PubKeyCredParams(
        val type: String = "public-key",
        val alg: Int = -7
    )

    @Serializable
    data class ExcludeCredentials(
        val id: String,
        val type: String
    )

    @Serializable
    data class AuthenticatorSelection(
        val authenticatorAttachment: String = "platform",
        val requireResidentKey: Boolean = false,
        val residentKey: String = "required",
        val userVerification: String = "required"
    )
}

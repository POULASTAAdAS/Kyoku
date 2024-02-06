package com.poulastaa.data.model.auth.passkey

import com.poulastaa.utils.Constants
import com.poulastaa.utils.Constants.AUTH_RESPONSE_PASSKEY_TYPE_SIGN_UP
import com.poulastaa.utils.generateFidoChallenge
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class CreatePasskeyJson(
    val token: String,
    val challenge: String = generateFidoChallenge(),
    val rp: Rp = Rp(),
    val user: User,
    val pubKeyCredParams: List<PubKeyCredParams> = listOf(
        PubKeyCredParams()
    ),
    val timeout: Long = 1800000,
    val attestation: String = "none",
    val excludeCredentials: List<ExcludeCredentials> = emptyList(),
    val authenticatorSelection: AuthenticatorSelection = AuthenticatorSelection()
) : PasskeyBaseModel(type = AUTH_RESPONSE_PASSKEY_TYPE_SIGN_UP) {
    @Serializable
    data class Rp(
        val name: String = "Kyoku",
        val id: String = Constants.BASE_URL.removePrefix("https://")
    )

    @Serializable
    data class User(
        val id: String = UUID.randomUUID().toString(),
        val name: String,
        val displayName: String
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

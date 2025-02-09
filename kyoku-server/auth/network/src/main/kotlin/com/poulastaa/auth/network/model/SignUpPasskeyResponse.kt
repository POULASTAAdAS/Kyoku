package com.poulastaa.auth.network.model

import com.poulastaa.auth.network.routes.utils.generateFidoChallenge
import kotlinx.serialization.Serializable
import java.util.*

internal data class SignUpPasskeyResponse(
    val type: PasskeyType = PasskeyType.SIGNUP,
    val challenge: String = generateFidoChallenge(),
    val rp: Rp = Rp(),
    val user: User,
    val pubKeyCredParams: List<PubKeyCredParams> = listOf(
        PubKeyCredParams()
    ),
    val timeout: Long = 1800000,
    val attestation: String = "none",
    val excludeCredentials: List<ExcludeCredentials> = emptyList(),
    val authenticatorSelection: AuthenticatorSelection = AuthenticatorSelection(),
) {
    @Serializable
    data class Rp(
        val name: String = "Kyoku",
        val id: String = System.getenv("BASE_URL").removePrefix("http://"),
    )

    @Serializable
    data class User(
        val id: String = UUID.randomUUID().toString(),
        val name: String,
        val displayName: String,
    )

    @Serializable
    data class PubKeyCredParams(
        val type: String = "public-key",
        val alg: Int = -7,
    )

    @Serializable
    data class ExcludeCredentials(
        val id: String,
        val type: String,
    )

    @Serializable
    data class AuthenticatorSelection(
        val authenticatorAttachment: String = "platform",
        val requireResidentKey: Boolean = false,
        val residentKey: String = "required",
    )
}

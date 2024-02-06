package com.poulastaa.kyoku.data.model.api.auth.passkey

import kotlinx.serialization.Serializable

@Serializable
data class CreatePublicKeyCredential(
    val rawId: String,
    val authenticatorAttachment: String,
    val type: String,
    val id: String,
    val response: Response,
    val clientExtensionResults: ClientExtensionResults
) {
    @Serializable
    data class Response(
        val clientDataJSON: String,
        val attestationObject: String,
        val transports: List<String>,
        val authenticatorData: String,
        val publicKeyAlgorithm: Int,
        val publicKey: String
    )

    @Serializable
    data class ClientExtensionResults(
        val credProps: CredProps
    ) {
        @Serializable
        data class CredProps(
            val rk: Boolean
        )
    }
}


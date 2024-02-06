package com.poulastaa.data.model.auth.passkey

import com.poulastaa.utils.Constants
import com.poulastaa.utils.Constants.AUTH_RESPONSE_PASSKEY_TYPE_LOGIN
import com.poulastaa.utils.generateFidoChallenge
import kotlinx.serialization.Serializable

@Serializable
data class GetPasskeyJson(
    val token: String,
    val challenge: String = generateFidoChallenge(),
    val allowCredentials: List<AllowCredentials>,
    val timeout: Long = 1800000,
    val userVerification: String = "required",
    val rpId: String = Constants.BASE_URL.removePrefix("https://")
) : PasskeyBaseModel(type = AUTH_RESPONSE_PASSKEY_TYPE_LOGIN) {
    @Serializable
    data class AllowCredentials(
        val id: String,
        val transports: List<String>,
        val type: String,
    )
}

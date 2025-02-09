package com.poulastaa.auth.network.model

import com.poulastaa.auth.network.routes.utils.generateFidoChallenge
import kotlinx.serialization.Serializable

internal data class LogInPasskeyResponse(
    val type: PasskeyType = PasskeyType.LOGIN,
    val challenge: String = generateFidoChallenge(),
    val allowCredentials: List<AllowCredentials>,
    val timeout: Long = 1800000,
    val userVerification: String = "required",
    val rpId: String = System.getenv("BASE_URL").removePrefix("http://"),
) {
    @Serializable
    data class AllowCredentials(
        val id: String,
        val transports: List<String>,
        val type: String,
    )
}

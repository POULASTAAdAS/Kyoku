package com.poulastaa.auth.network.res

import kotlinx.serialization.Serializable

@Serializable
data class EmailVerificationDto(
    val status: Boolean = false,
    val accessToken: String = "",
    val refreshToken: String = "",
)

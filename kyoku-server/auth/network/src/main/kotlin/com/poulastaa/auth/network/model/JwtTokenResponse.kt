package com.poulastaa.auth.network.model

import kotlinx.serialization.Serializable

@Serializable
data class JwtTokenResponse(
    val state: Boolean = false,
    val accessToken: String = "",
    val refreshToken: String = "",
)

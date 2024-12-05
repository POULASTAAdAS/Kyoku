package com.pouluastaa.auth.network.model

import kotlinx.serialization.Serializable

@Serializable
data class JwtTokenResponse(
    val accessToken: String = "",
    val refreshToken: String = "",
)

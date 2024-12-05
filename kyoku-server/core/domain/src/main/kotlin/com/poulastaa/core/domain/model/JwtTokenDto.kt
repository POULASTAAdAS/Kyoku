package com.poulastaa.core.domain.model

data class JwtTokenDto(
    val accessToken: String = "",
    val refreshToken: String = "",
)

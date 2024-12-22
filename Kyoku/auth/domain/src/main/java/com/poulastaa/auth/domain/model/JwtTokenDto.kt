package com.poulastaa.auth.domain.model

data class JwtTokenDto(
    val accessToken: String = "",
    val refreshToken: String = "",
)
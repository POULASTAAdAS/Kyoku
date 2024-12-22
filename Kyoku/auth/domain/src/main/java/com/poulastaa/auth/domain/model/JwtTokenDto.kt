package com.poulastaa.auth.domain.model

data class JwtTokenDto(
    val status: Boolean = false,
    val accessToken: String = "",
    val refreshToken: String = "",
)
package com.poulastaa.core.domain.model

data class EmailVerification(
    val status: Boolean = false,
    val accessToken: String = "",
    val refreshToken: String = "",
)
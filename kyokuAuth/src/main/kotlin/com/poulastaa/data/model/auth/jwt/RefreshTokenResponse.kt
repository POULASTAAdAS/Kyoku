package com.poulastaa.data.model.auth.jwt

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenResponse(
    val status: RefreshTokenUpdateStatus,
    val accessToken: String = "",
    val refreshToken: String = ""
)

package com.poulastaa.kyoku.data.model.api.auth.email

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenResponse(
    val status: RefreshTokenUpdateStatus,
    val accessToken: String = "",
    val refreshToken: String = ""
)

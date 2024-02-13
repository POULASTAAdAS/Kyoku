package com.poulastaa.kyoku.data.model.auth.email.reafresh_token

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenResponse(
    val status: RefreshTokenUpdateStatus,
    val accessToken: String = "",
    val refreshToken: String = ""
)

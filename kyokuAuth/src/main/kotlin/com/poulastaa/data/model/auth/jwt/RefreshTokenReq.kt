package com.poulastaa.data.model.auth.jwt

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenReq(
    val oldRefreshToken: String
)

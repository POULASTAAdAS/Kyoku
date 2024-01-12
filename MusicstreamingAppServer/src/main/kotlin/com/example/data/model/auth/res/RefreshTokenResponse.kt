package com.example.data.model.auth.res

import com.example.data.model.auth.stat.RefreshTokenUpdateStatus
import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenResponse(
    val status: RefreshTokenUpdateStatus,
    val accessToken: String = "",
    val refreshToken: String = ""
)

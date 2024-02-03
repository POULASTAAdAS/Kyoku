package com.poulastaa.data.model.auth.res

import com.poulastaa.data.model.auth.stat.RefreshTokenUpdateStatus
import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenResponse(
    val status: RefreshTokenUpdateStatus,
    val accessToken: String = "",
    val refreshToken: String = ""
)

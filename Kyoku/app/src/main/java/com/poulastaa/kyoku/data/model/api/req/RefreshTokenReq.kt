package com.poulastaa.kyoku.data.model.api.req

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenReq(
    val oldRefreshToken: String
)

package com.poulastaa.kyoku.data.model.auth.email.reafresh_token

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenReq(
    val oldRefreshToken: String
)

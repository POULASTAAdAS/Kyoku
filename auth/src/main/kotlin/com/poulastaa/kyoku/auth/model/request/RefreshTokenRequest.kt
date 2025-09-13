package com.poulastaa.kyoku.auth.model.request

import com.poulastaa.kyoku.auth.utils.Email
import com.poulastaa.kyoku.auth.utils.JWTToken

data class RefreshTokenRequest(
    val email: Email,
    val type: String,
    val oldToken: JWTToken,
)

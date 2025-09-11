package com.poulastaa.kyoku.auth.model.response

import com.poulastaa.kyoku.auth.utils.JWTToken

data class ResponseToken(
    val accessToken: JWTToken = "",
    val refreshToken: JWTToken = "",
)

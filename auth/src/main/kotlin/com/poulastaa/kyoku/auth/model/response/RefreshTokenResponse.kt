package com.poulastaa.kyoku.auth.model.response

data class RefreshTokenResponse(
    val status: RefreshTokenResponseStatus = RefreshTokenResponseStatus.TOKEN_EXPIRED,
    val payload: ResponseToken = ResponseToken(),
)

package com.poulastaa.kyoku.auth.model.response

data class ResponseGoogleAuth(
    val user: ResponseUser = ResponseUser(),
    val token: ResponseToken = ResponseToken(),
)

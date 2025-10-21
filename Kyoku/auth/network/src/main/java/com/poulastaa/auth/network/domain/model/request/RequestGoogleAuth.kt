package com.poulastaa.auth.network.domain.model.request

import com.poulastaa.core.domain.utils.JWTToken

data class RequestGoogleAuth(
    val token: JWTToken,
    val code: String,
)

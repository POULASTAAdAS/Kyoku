package com.poulastaa.kyoku.auth.model.request

import com.poulastaa.kyoku.auth.utils.GoogleJWTToken

data class GoogleAuth(
    val token: GoogleJWTToken,
    val code: String,
)

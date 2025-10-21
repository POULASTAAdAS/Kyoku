package com.poulastaa.kyoku.auth.model.request

import com.poulastaa.kyoku.auth.utils.JWTToken
import com.poulastaa.kyoku.auth.utils.Password

data class UpdatePassword(
    val password: Password,
    val token: JWTToken,
)

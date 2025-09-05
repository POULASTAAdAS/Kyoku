package com.poulastaa.kyoku.auth.model.request

import com.poulastaa.kyoku.auth.utils.Email
import com.poulastaa.kyoku.auth.utils.Password
import com.poulastaa.kyoku.auth.utils.Username

data class EmailSignUp(
    val username: Username,
    val email: Email,
    val password: Password,
    val countryCode: String,
)

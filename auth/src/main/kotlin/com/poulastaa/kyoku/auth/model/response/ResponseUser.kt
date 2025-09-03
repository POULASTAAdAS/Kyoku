package com.poulastaa.kyoku.auth.model.response

import com.poulastaa.kyoku.auth.utils.Email
import com.poulastaa.kyoku.auth.utils.Username

data class ResponseUser(
    val email: Email = "",
    val username: Username = "",
    val profileUrl: String? = null,
)

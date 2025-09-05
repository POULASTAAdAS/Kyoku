package com.poulastaa.kyoku.auth.model.response

import com.poulastaa.kyoku.auth.model.dto.UserType
import com.poulastaa.kyoku.auth.utils.Email
import com.poulastaa.kyoku.auth.utils.Username

data class ResponseUser(
    val status: UserStatus = UserStatus.USER_NOT_FOUND,
    val email: Email = "",
    val username: Username = "",
    val profileUrl: String? = null,
    val type: UserType = UserType.DEFAULT,
)

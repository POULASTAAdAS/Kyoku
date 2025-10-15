package com.poulastaa.auth.domain.model.response

import com.poulastaa.core.domain.Email
import com.poulastaa.core.domain.Username

data class ResponseUser(
    val status: DtoAuthResponseStatus = DtoAuthResponseStatus.USER_NOT_FOUND,
    val email: Email = "",
    val username: Username = "",
    val profileUrl: String? = null,
    val type: UserType = UserType.DEFAULT,
)
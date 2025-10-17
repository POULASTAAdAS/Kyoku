package com.poulastaa.auth.domain.model.response

import com.poulastaa.auth.domain.model.DtoAuthResponseStatus
import com.poulastaa.auth.domain.model.DtoUserType
import com.poulastaa.core.domain.Email
import com.poulastaa.core.domain.Username

data class ResponseUser(
    val status: DtoAuthResponseStatus = DtoAuthResponseStatus.USER_NOT_FOUND,
    val email: Email = "",
    val username: Username = "",
    val profileUrl: String? = null,
    val type: DtoUserType = DtoUserType.DEFAULT,
)
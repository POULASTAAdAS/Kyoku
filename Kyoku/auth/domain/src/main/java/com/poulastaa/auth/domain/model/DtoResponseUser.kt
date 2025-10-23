package com.poulastaa.auth.domain.model

import com.poulastaa.core.domain.model.DtoUserType
import com.poulastaa.core.domain.utils.Email
import com.poulastaa.core.domain.utils.Username

data class DtoResponseUser(
    val status: DtoAuthResponseStatus = DtoAuthResponseStatus.USER_NOT_FOUND,
    val email: Email = "",
    val username: Username = "",
    val profileUrl: String? = null,
    val type: DtoUserType = DtoUserType.DEFAULT,
)
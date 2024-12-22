package com.poulastaa.auth.domain.model

import com.poulastaa.core.domain.model.UserDto

data class AuthResponseDto(
    val status: AuthStatus = AuthStatus.SERVER_ERROR,
    val user: UserDto,
)

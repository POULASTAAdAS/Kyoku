package com.poulastaa.auth.domain.model

import com.poulastaa.core.domain.model.UserDto

data class AuthResponseDto(
    val status: AuthResponseStatusDto = AuthResponseStatusDto.SERVER_ERROR,
    val user: UserDto = UserDto(),
)

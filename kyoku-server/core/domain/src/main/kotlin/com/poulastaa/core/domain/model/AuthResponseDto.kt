package com.poulastaa.core.domain.model

data class AuthResponseDto(
    val status: AuthResponseStatusDto = AuthResponseStatusDto.SERVER_ERROR,
    val user: UserDto = UserDto(),
    val token: JwtTokenDto = JwtTokenDto(),
)

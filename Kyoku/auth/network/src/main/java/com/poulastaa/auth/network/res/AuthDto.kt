package com.poulastaa.auth.network.res

import kotlinx.serialization.Serializable

@Serializable
data class AuthDto(
    val status: UserAuthStatusDto = UserAuthStatusDto.USER_NOT_FOUND,
    val user: ResponseUserDto = ResponseUserDto(),
    val loginDto: LogInDto = LogInDto(),
)
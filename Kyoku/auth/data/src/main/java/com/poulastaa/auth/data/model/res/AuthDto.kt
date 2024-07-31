package com.poulastaa.auth.data.model.res

import com.poulastaa.auth.data.model.res.login.LogInDto
import kotlinx.serialization.Serializable

@Serializable
data class AuthDto(
    val status: UserAuthStatusDto = UserAuthStatusDto.USER_NOT_FOUND,
    val user: ResponseUserDto = ResponseUserDto(),
    val loginDto: LogInDto = LogInDto(),
)
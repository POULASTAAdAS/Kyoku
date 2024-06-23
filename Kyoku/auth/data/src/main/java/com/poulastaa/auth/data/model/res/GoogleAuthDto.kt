package com.poulastaa.auth.data.model.res

import kotlinx.serialization.Serializable

@Serializable
data class GoogleAuthDto(
    val status: UserAuthStatusDto = UserAuthStatusDto.USER_NOT_FOUND,
    val user: ResponseUserDto = ResponseUserDto()
)
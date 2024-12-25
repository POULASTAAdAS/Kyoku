package com.poulastaa.auth.data.mapper

import com.poulastaa.core.domain.model.DBUserDto
import com.poulastaa.core.domain.model.UserDto

fun DBUserDto.toUserDto() = UserDto(
    id = id,
    email = email,
    username = userName,
    profilePicUrl = profilePicUrl
)
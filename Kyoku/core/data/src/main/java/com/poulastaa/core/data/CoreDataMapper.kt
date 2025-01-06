package com.poulastaa.core.data

import com.poulastaa.core.data.model.UserSerializable
import com.poulastaa.core.domain.model.UserDto

fun UserSerializable.toUser() = UserDto(
    name = name,
    email = email,
    profilePic = profilePic
)

fun UserDto.toUserSerializable() = UserSerializable(
    name = name,
    email = email,
    profilePic = profilePic
)

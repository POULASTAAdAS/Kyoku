package com.poulastaa.core.data

import com.poulastaa.core.data.model.UserSerializable
import com.poulastaa.core.domain.model.DtoUser

fun UserSerializable.toUser() = DtoUser(
    name = name,
    email = email,
    profilePic = profilePic
)

fun DtoUser.toUserSerializable() = UserSerializable(
    name = name,
    email = email,
    profilePic = profilePic
)

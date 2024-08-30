package com.poulastaa.core.data.auth

import com.poulastaa.core.data.auth.model.UserSerializable
import com.poulastaa.core.domain.model.User

fun User.toUserSerializable() = UserSerializable(
    name = this.name,
    email = this.email,
    profilePic = this.profilePic
)

fun UserSerializable.toUser() = User(
    name = this.name,
    email = this.email,
    profilePic = this.profilePic
)
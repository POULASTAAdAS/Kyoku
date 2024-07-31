package com.poulastaa.auth.data.mapper

import com.poulastaa.core.domain.User
import com.poulastaa.core.domain.model.ResponseUser


fun ResponseUser.toUser() = User(
    name = this.userName,
    email = this.email,
    profilePic = this.profilePic
)
package com.poulastaa.auth.data.mapper

import com.poulastaa.core.domain.model.ResponseUser
import com.poulastaa.core.domain.model.User


fun ResponseUser.toUser() = User(
    name = this.userName,
    email = this.email,
    profilePic = this.profilePic
)
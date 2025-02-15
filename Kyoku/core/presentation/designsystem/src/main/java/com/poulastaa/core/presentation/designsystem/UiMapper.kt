package com.poulastaa.core.presentation.designsystem

import com.poulastaa.core.domain.model.DtoUser
import com.poulastaa.core.presentation.designsystem.model.UiUser

fun DtoUser.toUiUser() = UiUser(
    username = this.name.ifEmpty { "User" },
    email = this.email,
    profilePic = this.profilePic
)
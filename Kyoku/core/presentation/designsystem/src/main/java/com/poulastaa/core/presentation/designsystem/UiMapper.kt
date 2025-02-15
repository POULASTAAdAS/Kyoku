package com.poulastaa.core.presentation.designsystem

import com.poulastaa.core.domain.model.DtoUser
import com.poulastaa.core.presentation.designsystem.model.UiUser

fun DtoUser.toUiUser() = UiUser(
    username = this.name,
    email = this.email,
    profilePic = this.profilePic
)
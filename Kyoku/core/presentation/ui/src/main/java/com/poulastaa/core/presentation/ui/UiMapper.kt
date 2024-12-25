package com.poulastaa.core.presentation.ui

import com.poulastaa.core.domain.model.UserDto
import com.poulastaa.core.presentation.ui.model.UiUser

fun UserDto.toUiUser() = UiUser(
    username = this.name,
    email = this.email,
    profilePic = this.profilePic
)
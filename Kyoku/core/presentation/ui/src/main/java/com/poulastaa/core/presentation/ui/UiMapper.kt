package com.poulastaa.core.presentation.ui

import com.poulastaa.core.domain.model.DtoUser
import com.poulastaa.core.presentation.ui.model.UiUser

fun DtoUser.toUiUser() = UiUser(
    username = this.name,
    email = this.email,
    profilePic = this.profilePic
)
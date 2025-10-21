package com.poulastaa.auth.data.mapper

import com.poulastaa.auth.domain.model.DtoResponseUser
import com.poulastaa.core.domain.model.DtoUser

internal fun DtoResponseUser.toDtoUser() = DtoUser(
    username = this.username,
    email = this.email,
    profileUrl = this.profileUrl,
    type = this.type,
)
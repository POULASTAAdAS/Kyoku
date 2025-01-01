package com.poulastaa.auth.data.mapper

import com.poulastaa.core.domain.model.DtoDBUser
import com.poulastaa.core.domain.model.DtoUser

fun DtoDBUser.toUserDto() = DtoUser(
    id = id,
    email = email,
    username = userName,
    profilePicUrl = profilePicUrl
)
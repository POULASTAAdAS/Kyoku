package com.poulastaa.kyoku.gateway.model

import com.poulastaa.kyoku.gateway.utils.*

data class DtoUser(
    val userId: UserId,
    val username: Username,
    val email: Email,
    val type: UserType,
)

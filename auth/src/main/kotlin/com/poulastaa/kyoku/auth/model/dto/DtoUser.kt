package com.poulastaa.kyoku.auth.model.dto

import com.poulastaa.kyoku.auth.utils.Email
import com.poulastaa.kyoku.auth.utils.Password
import com.poulastaa.kyoku.auth.utils.UserId
import com.poulastaa.kyoku.auth.utils.Username
import java.time.LocalDateTime

data class DtoUser(
    val id: UserId = -1,
    val username: Username = "",
    val email: Email = "",
    val passwordHash: Password = "",
    val type: UserType = UserType.EMAIL,
    val profileUrl: String? = null,
    val lastUpdated: LocalDateTime = LocalDateTime.now(),
)

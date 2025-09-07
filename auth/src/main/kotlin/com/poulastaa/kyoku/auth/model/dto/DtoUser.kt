package com.poulastaa.kyoku.auth.model.dto

import com.poulastaa.kyoku.auth.utils.Email
import com.poulastaa.kyoku.auth.utils.Password
import com.poulastaa.kyoku.auth.utils.UserId
import com.poulastaa.kyoku.auth.utils.Username
import java.time.LocalDate

data class DtoUser(
    val id: UserId = -1,
    val username: Username,
    val displayName: Username,
    val email: Email,
    val passwordHash: Password,
    val countryCode: String,
    val type: UserType,
    val profileUrl: String? = null,
    val birthDate: LocalDate? = null,
)

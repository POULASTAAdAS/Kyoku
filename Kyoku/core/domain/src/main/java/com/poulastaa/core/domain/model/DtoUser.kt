package com.poulastaa.core.domain.model

import com.poulastaa.core.domain.utils.Email
import com.poulastaa.core.domain.utils.Username
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

data class DtoUser(
    val username: Username,
    val email: Email,
    val profileUrl: String? = null,
    val type: DtoUserType,
    val bDate: LocalDate? = null,
)

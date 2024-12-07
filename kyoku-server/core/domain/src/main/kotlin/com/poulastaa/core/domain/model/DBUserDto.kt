package com.poulastaa.core.domain.model

import java.time.LocalDate

data class DBUserDto(
    val id: Long,
    val email: String,
    val userName: String,
    val passwordHash: String,
    val profilePicUrl: String?,
    val countryCode: Int,
    val bDate: LocalDate?,
)

package com.poulastaa.core.domain.model

import java.time.LocalDate

data class DtoDBUser(
    val id: Long,
    val email: String,
    val userName: String,
    val passwordHash: String,
    val profilePicUrl: String? = null,
    val countryId: Int,
    val bDate: LocalDate? = null,
)

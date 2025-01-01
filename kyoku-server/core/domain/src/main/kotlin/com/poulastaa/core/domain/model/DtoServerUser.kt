package com.poulastaa.core.domain.model

data class DtoServerUser(
    val email: String,
    val type: UserType,
    val username: String,
    val password: String,
    val profilePicUrl: String? = null,
    val countryId: Int,
)

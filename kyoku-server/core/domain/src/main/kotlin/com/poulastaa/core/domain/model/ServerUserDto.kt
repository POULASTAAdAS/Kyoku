package com.poulastaa.core.domain.model

data class ServerUserDto(
    val email: String,
    val username: String,
    val password: String,
    val profilePicUrl: String,
    val countryId: Int,
)

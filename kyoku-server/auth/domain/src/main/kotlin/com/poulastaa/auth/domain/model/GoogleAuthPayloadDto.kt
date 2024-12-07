package com.poulastaa.auth.domain.model

data class GoogleAuthPayloadDto(
    val sub: String,
    val email: String,
    val userName: String,
    val profilePicUrl: String,
)
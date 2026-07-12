package com.poulastaa.common.domain.model

data class DtoUser(
    val userId: Long,
    val username: String,
    val email: String,
    val profilePictureUrl: String? = null,
)
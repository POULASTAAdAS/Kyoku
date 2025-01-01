package com.poulastaa.core.domain.model

data class DtoUser(
    val id: Long = -1,
    val email: String = "",
    val username: String = "",
    val profilePicUrl: String? = null,
)

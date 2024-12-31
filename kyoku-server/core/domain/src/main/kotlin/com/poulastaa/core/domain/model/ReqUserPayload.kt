package com.poulastaa.core.domain.model

data class ReqUserPayload(
    val id: Long? = null,
    val email: String,
    val userType: UserType,
)

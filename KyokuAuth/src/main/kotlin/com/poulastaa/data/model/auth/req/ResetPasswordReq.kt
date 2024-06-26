package com.poulastaa.data.model.auth.req

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordReq(
    val password: String,
    val token: String,
)

package com.poulastaa.auth.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordRequest(
    val password: String,
    val token: String,
)

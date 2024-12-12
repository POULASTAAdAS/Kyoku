package com.poulastaa.auth.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NewPasswordRequest(
    val token: String,
    val password: String,
)

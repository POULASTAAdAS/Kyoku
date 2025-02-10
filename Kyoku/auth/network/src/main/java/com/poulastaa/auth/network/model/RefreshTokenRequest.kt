package com.poulastaa.auth.network.model

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequest(
    val email: String,
    val token: String,
)

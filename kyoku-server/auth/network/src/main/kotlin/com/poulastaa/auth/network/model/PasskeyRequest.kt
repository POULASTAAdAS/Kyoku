package com.poulastaa.auth.network.model

import kotlinx.serialization.Serializable

@Serializable
data class PasskeyRequest(
    val email: String,
)

package com.poulastaa.auth.network.model

import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    val email: String,
    val username: String,
    val password: String,
    val countryCode: String,
)

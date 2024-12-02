package com.pouluastaa.auth.network.model

import kotlinx.serialization.Serializable

@Serializable
data class EmailSignUpRequest(
    val email: String,
    val password: String,
    val username: String,
    val countryCode: String,
)

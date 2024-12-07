package com.poulastaa.auth.domain.model

data class EmailSignUpPayload(
    val email: String,
    val password: String,
    val username: String,
    val countryCode: String,
)

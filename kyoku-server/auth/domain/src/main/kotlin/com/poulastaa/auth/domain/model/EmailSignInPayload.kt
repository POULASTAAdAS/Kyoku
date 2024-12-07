package com.poulastaa.auth.domain.model

data class EmailSignInPayload(
    val email: String,
    val password: String,
)

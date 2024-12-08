package com.poulastaa.auth.domain.model

data class EmailLogInPayload(
    val email: String,
    val password: String,
)

package com.pouluastaa.auth.network.model

import kotlinx.serialization.Serializable

@Serializable
data class EmailLogInRequest(
    val email: String,
    val password: String,
)

package com.poulastaa.data.model.auth.response

import kotlinx.serialization.Serializable

@Serializable
data class CheckEmailVerificationResponse(
    val status: Boolean = false,
    val accessToken: String = "",
    val refreshToken: String = "",
)

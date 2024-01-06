package com.example.data.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class EmailVerificationResponse(
    val status: EmailVerificationStatus
)

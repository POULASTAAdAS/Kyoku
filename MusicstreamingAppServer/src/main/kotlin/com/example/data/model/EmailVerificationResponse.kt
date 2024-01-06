package com.example.data.model

import com.example.routes.auth.common.EmailVerificationStatus
import kotlinx.serialization.Serializable

@Serializable
data class EmailVerificationResponse(
    val status: EmailVerificationStatus
)

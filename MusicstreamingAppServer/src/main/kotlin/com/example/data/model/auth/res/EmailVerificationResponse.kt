package com.example.data.model.auth.res

import com.example.data.model.auth.stat.EmailVerificationStatus
import kotlinx.serialization.Serializable

@Serializable
data class EmailVerificationResponse(
    val status: EmailVerificationStatus
)

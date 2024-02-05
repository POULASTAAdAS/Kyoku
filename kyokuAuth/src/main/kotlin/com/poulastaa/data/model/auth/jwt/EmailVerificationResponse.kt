package com.poulastaa.data.model.auth.jwt

import kotlinx.serialization.Serializable

@Serializable
data class EmailVerificationResponse(
    val status: EmailVerificationStatus
)

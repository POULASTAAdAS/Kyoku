package com.poulastaa.kyoku.data.model.screens.auth.email.signup

import kotlinx.serialization.Serializable

@Serializable
data class EmailVerificationResponse(
    val status: EmailVerificationStatus
)

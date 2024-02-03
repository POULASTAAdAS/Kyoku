package com.poulastaa.data.model.auth.res

import com.poulastaa.data.model.auth.stat.EmailVerificationStatus
import kotlinx.serialization.Serializable

@Serializable
data class EmailVerificationResponse(
    val status: EmailVerificationStatus
)

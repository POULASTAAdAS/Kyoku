package com.poulastaa.data.model.payload

import com.poulastaa.data.model.VerifiedMailStatus

data class UpdateEmailVerificationPayload(
    val username: String = "",
    val status: VerifiedMailStatus = VerifiedMailStatus.USER_NOT_FOUND,
)

package com.poulastaa.kyoku.data.model.api.auth.email

import kotlinx.serialization.Serializable

@Serializable
data class ResendVerificationMailResponse(
    val status: ResendVerificationMailStatus
)

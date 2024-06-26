package com.poulastaa.data.model.auth.response

import com.poulastaa.data.model.payload.UpdatePasswordStatus
import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordRes(
    val status: UpdatePasswordStatus,
    val successUrl: String,
)

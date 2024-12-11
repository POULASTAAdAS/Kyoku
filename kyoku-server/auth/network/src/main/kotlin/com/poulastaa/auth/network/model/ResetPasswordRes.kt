package com.poulastaa.auth.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordRes(
    val status: UpdatePasswordStatus,
    val successUrl: String,
)

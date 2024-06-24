package com.poulastaa.data.model.auth.res

import kotlinx.serialization.Serializable

@Serializable
data class EmailVerificationRes(
    val status: Boolean = false,
)

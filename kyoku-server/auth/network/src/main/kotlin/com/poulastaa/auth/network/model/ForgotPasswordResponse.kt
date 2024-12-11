package com.poulastaa.auth.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ForgotPasswordResponse(
    val status: ForgotPasswordResponseStatus = ForgotPasswordResponseStatus.SERVER_ERROR,
)

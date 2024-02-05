package com.poulastaa.data.model.auth.jwt

import kotlinx.serialization.Serializable

@Serializable
data class SendForgotPasswordMail(
    val status: SendVerificationMailStatus
)

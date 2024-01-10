package com.example.data.model.auth.stat

import kotlinx.serialization.Serializable

@Serializable
data class SendForgotPasswordMail(
    val status: SendVerificationMailStatus
)

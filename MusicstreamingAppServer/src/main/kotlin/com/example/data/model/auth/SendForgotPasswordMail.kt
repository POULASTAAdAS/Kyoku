package com.example.data.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class SendForgotPasswordMail(
    val status: SendVerificationMailStatus
)

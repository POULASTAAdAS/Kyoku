package com.example.data.model

import com.example.routes.auth.common.SendVerificationMailStatus
import kotlinx.serialization.Serializable

@Serializable
data class SendForgotPasswordMail(
    val status: SendVerificationMailStatus
)

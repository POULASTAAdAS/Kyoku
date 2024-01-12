package com.example.data.model.auth.res

import com.example.data.model.auth.stat.SendVerificationMailStatus
import kotlinx.serialization.Serializable

@Serializable
data class SendForgotPasswordMail(
    val status: SendVerificationMailStatus
)

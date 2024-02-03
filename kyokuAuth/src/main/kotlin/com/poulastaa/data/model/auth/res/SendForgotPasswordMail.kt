package com.poulastaa.data.model.auth.res

import com.poulastaa.data.model.auth.stat.SendVerificationMailStatus
import kotlinx.serialization.Serializable

@Serializable
data class SendForgotPasswordMail(
    val status: SendVerificationMailStatus
)

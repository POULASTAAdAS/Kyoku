package com.poulastaa.kyoku.data.model.api.auth.email

import kotlinx.serialization.Serializable

@Serializable
data class SendForgotPasswordMail(
    val status: SendForgotPasswordMailStatus
)

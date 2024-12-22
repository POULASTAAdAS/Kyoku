package com.poulastaa.auth.network.model

import kotlinx.serialization.Serializable

@Serializable
enum class ForgotPasswordResponseStatus {
    FORGOT_PASSWORD_MAIL_SEND,
    USER_NOT_FOUND,
    SERVER_ERROR,
    EMAIL_NOT_PROVIDED,
    EMAIL_NOT_VALID,
}
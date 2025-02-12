package com.poulastaa.auth.domain.model

enum class ForgotPasswordStatus {
    FORGOT_PASSWORD_MAIL_SEND,
    USER_NOT_FOUND,
    SERVER_ERROR,
    EMAIL_NOT_PROVIDED,
    EMAIL_NOT_VALID,
}
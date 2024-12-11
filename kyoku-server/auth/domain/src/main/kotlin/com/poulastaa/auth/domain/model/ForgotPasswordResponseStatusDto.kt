package com.poulastaa.auth.domain.model

enum class ForgotPasswordResponseStatusDto {
    FORGOT_PASSWORD_MAIL_SEND,
    USER_NOT_FOUND,
    SERVER_ERROR,
    EMAIL_NOT_PROVIDED,
    EMAIL_NOT_VALID,
}
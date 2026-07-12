package com.poulastaa.auth.domain.model

data class DtoForgotPasswordResponse(
    val status: DtoForgotPasswordStatus,
)

enum class DtoForgotPasswordStatus {
    SENT,
    USER_NOT_FOUND,
    INVALID_EMAIL,
    ERROR,
}

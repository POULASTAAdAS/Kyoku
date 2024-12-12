package com.poulastaa.auth.domain.model

enum class ResetPasswordStatusDto {
    TOKEN_VALID,
    TOKEN_USED,
    TOKEN_EXPIRED,
    SERVER_ERROR
}
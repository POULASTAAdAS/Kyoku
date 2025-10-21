package com.poulastaa.auth.domain.model

enum class DtoResetPasswordState {
    UPDATED,
    USER_NOT_FOUND,
    SAME_PASSWORD,
    INVALID_PASSWORD,
    EXPIRED_TOKEN,
    ERROR,
    INVALID_TOKEN
}
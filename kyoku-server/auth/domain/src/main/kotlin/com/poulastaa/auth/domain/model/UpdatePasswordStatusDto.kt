package com.poulastaa.auth.domain.model

enum class UpdatePasswordStatusDto {
    RESET,
    SAME_PASSWORD,
    USER_NOT_FOUND,
    SERVER_ERROR,
    TOKEN_USED,
}
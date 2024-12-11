package com.poulastaa.auth.domain.model

enum class EmailVerificationStatusDto {
    VERIFIED,
    TOKEN_USED,
    USER_NOT_FOUND,
    SERVER_ERROR
}
package com.poulastaa.kyoku.auth.model.dto

enum class EmailVerificationStatus {
    VALID,
    TOKEN_ALREADY_USED,
    TOKEN_EXPIRED,
    USER_NOT_FOUND
}
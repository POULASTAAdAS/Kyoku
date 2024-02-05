package com.poulastaa.data.model.auth.jwt

enum class PasswordResetStatus {
    SUCCESSFUL,
    SAME_AS_OLD_PASSWORD,
    USER_NOT_FOUND,
    TOKEN_USED,
    TOKEN_EXPIRED,
    SOMETHING_WENT_WRONG
}
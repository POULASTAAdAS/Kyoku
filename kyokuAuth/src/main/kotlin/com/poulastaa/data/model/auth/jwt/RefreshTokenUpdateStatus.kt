package com.poulastaa.data.model.auth.jwt

enum class RefreshTokenUpdateStatus {
    TOKEN_EXPIRED,
    UPDATED,
    USER_NOT_FOUND,
    DUPLICATE_TOKEN,
    SOMETHING_WENT_WRONG
}
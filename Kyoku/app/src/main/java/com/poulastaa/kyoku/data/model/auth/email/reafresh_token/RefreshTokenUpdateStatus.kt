package com.poulastaa.kyoku.data.model.auth.email.reafresh_token

enum class RefreshTokenUpdateStatus {
    TOKEN_EXPIRED,
    UPDATED,
    USER_NOT_FOUND,
    DUPLICATE_TOKEN,
    SOMETHING_WENT_WRONG
}
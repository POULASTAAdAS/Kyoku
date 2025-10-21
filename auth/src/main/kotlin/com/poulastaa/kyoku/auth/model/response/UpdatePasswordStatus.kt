package com.poulastaa.kyoku.auth.model.response

enum class UpdatePasswordStatus {
    UPDATED,
    USER_NOT_FOUND,
    SAME_PASSWORD,
    INVALID_PASSWORD,
    EXPIRED_TOKEN,
    ERROR,
    INVALID_TOKEN
}
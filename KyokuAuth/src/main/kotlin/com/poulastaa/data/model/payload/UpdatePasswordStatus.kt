package com.poulastaa.data.model.payload

enum class UpdatePasswordStatus {
    RESET,
    SAME_PASSWORD,
    USER_NOT_FOUND,
    NOT_PASSWORD,
    TOKEN_USED,
}
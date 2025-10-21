package com.poulastaa.kyoku.auth.model.response

enum class CodeValidationResponseStatus {
    VALID,
    USER_NOT_FOUND,
    INVALID_CODE,
    ERROR,
    INVALID_EMAIL,
    EXPIRED
}
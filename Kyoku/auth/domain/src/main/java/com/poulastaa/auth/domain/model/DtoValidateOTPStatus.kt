package com.poulastaa.auth.domain.model

enum class DtoValidateOTPStatus {
    VALID,
    USER_NOT_FOUND,
    INVALID_CODE,
    INVALID_EMAIL,
    EXPIRED
}
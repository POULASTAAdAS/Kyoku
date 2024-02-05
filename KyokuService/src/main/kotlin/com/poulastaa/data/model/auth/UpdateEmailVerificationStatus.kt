package com.poulastaa.data.model.auth

enum class UpdateEmailVerificationStatus {
    VERIFIED,
    TOKEN_USED,
    USER_NOT_FOUND,
    TOKEN_NOT_VALID,
    SOMETHING_WENT_WRONG
}
package com.poulastaa.kyoku.auth.model.dto

enum class JWTTokenType {
    TOKEN_ACCESS,
    TOKEN_REFRESH,
    TOKEN_VERIFICATION_MAIL,
    TOKEN_FORGOT_PASSWORD,
    TOKEN_SUBMIT_NEW_PASSWORD
}
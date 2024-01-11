package com.example.domain.repository.jwt

import com.example.util.Constants.ACCESS_TOKEN_CLAIM_KEY
import com.example.util.Constants.ACCESS_TOKEN_DEFAULT_TIME
import com.example.util.Constants.FORGOT_PASSWORD_MAIL_TOKEN_CLAIM_KEY
import com.example.util.Constants.FORGOT_PASSWORD_MAIL_TOKEN_TIME
import com.example.util.Constants.REFRESH_TOKEN_CLAIM_KEY
import com.example.util.Constants.REFRESH_TOKEN_DEFAULT_TIME
import com.example.util.Constants.VERIFICATION_MAIL_TOKEN_CLAIM_KEY
import com.example.util.Constants.VERIFICATION_MAIL_TOKEN_TIME

interface JWTRepository {
    fun generateAccessToken(
        sub: String = "Authentication",
        email: String,
        claimName: String = ACCESS_TOKEN_CLAIM_KEY,
        validationTime: Long = ACCESS_TOKEN_DEFAULT_TIME
    ): String

    fun generateRefreshToken(
        sub: String = "Authentication",
        email: String,
        claimName: String = REFRESH_TOKEN_CLAIM_KEY,
        validationTime: Long = REFRESH_TOKEN_DEFAULT_TIME
    ): String

    fun generateVerificationMailToken(
        sub: String = "VerificationMail",
        email: String,
        claimName: String = VERIFICATION_MAIL_TOKEN_CLAIM_KEY,
        validationTime: Long = VERIFICATION_MAIL_TOKEN_TIME
    ): String

    fun generateForgotPasswordMailToken(
        sub: String = "ForgotPasswordMail",
        email: String,
        claimName: String = FORGOT_PASSWORD_MAIL_TOKEN_CLAIM_KEY,
        validationTime: Long = FORGOT_PASSWORD_MAIL_TOKEN_TIME
    ): String

    fun verifyJWTToken(token: String, claim: String): String?
}
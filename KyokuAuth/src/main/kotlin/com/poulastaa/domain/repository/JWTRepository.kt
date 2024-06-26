package com.poulastaa.domain.repository

import com.poulastaa.utils.Constants.ACCESS_TOKEN_CLAIM_KEY
import com.poulastaa.utils.Constants.ACCESS_TOKEN_DEFAULT_TIME
import com.poulastaa.utils.Constants.FORGOT_PASSWORD_MAIL_TOKEN_CLAIM_KEY
import com.poulastaa.utils.Constants.FORGOT_PASSWORD_MAIL_TOKEN_TIME
import com.poulastaa.utils.Constants.REFRESH_TOKEN_CLAIM_KEY
import com.poulastaa.utils.Constants.REFRESH_TOKEN_DEFAULT_TIME
import com.poulastaa.utils.Constants.SUBMIT_NEW_PASSWORD_TOKEN_CLAIM_KEY
import com.poulastaa.utils.Constants.SUBMIT_NEW_PASSWORD_TOKEN_TIME
import com.poulastaa.utils.Constants.VERIFICATION_MAIL_TOKEN_CLAIM_KEY
import com.poulastaa.utils.Constants.VERIFICATION_MAIL_TOKEN_TIME

interface JWTRepository {
    fun generateAccessToken(
        sub: String = "Authentication",
        email: String,
        claimName: String = ACCESS_TOKEN_CLAIM_KEY,
        validationTime: Long = ACCESS_TOKEN_DEFAULT_TIME,
    ): String

    fun generateRefreshToken(
        sub: String = "Authentication",
        email: String,
        claimName: String = REFRESH_TOKEN_CLAIM_KEY,
        validationTime: Long = REFRESH_TOKEN_DEFAULT_TIME,
    ): String

    fun generateVerificationMailToken(
        sub: String = "VerificationMail",
        email: String,
        claimName: String = VERIFICATION_MAIL_TOKEN_CLAIM_KEY,
        validationTime: Long = VERIFICATION_MAIL_TOKEN_TIME,
    ): String

    fun generateForgotPasswordMailToken(
        sub: String = "ForgotPasswordMail",
        email: String,
        claimName: String = FORGOT_PASSWORD_MAIL_TOKEN_CLAIM_KEY,
        validationTime: Long = FORGOT_PASSWORD_MAIL_TOKEN_TIME,
    ): String

    fun generateSubmitPasswordVerificationToken(
        sub: String = "SubmitNewPassword",
        email: String,
        claimName: String = SUBMIT_NEW_PASSWORD_TOKEN_CLAIM_KEY,
        validationTime: Long = SUBMIT_NEW_PASSWORD_TOKEN_TIME,
    ): String

    fun verifyJWTToken(token: String, claim: String): String?

    fun getIssuer(): String
    fun getRealm(): String
}
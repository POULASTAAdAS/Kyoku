package com.poulastaa.core.domain.repository.auth

import kotlin.time.DurationUnit
import kotlin.time.toDuration

typealias JWTToken = String

interface JWTRepository {
    fun generateToken(
        email: String,
        type: TokenType,
    ): JWTToken

    fun verifyToken(
        token: String,
        type: TokenType,
    ): Email?

    enum class TokenType(
        val sub: String,
        val claimKey: String,
        val validationTime: Long,
    ) {
        TOKEN_ACCESS(
            sub = "AUTH_ACCESS",
            claimKey = "ACCESS_TOKEN_KEY",
            validationTime = 6.toDuration(DurationUnit.HOURS).inWholeMilliseconds,
        ),
        TOKEN_REFRESH(
            sub = "TOKEN_REFRESH",
            claimKey = "REFRESH_TOKEN_KEY",
            validationTime = 7.toDuration(DurationUnit.DAYS).inWholeMilliseconds,
        ),
        TOKEN_VERIFICATION_MAIL(
            sub = "TOKEN_VERIFICATION_MAIL",
            claimKey = "VERIFICATION_MAIL_TOKEN_KEY",
            validationTime = 10.toDuration(DurationUnit.MINUTES).inWholeMilliseconds,
        ),
        TOKEN_FORGOT_PASSWORD(
            sub = "TOKEN_FORGOT_PASSWORD",
            claimKey = "FORGOT_PASSWORD_TOKEN_KEY",
            validationTime = 10.toDuration(DurationUnit.MINUTES).inWholeMilliseconds,
        ),
        TOKEN_SUBMIT_NEW_PASSWORD(
            sub = "TOKEN_SUBMIT_NEW_PASSWORD",
            claimKey = "SUBMIT_NEW_PASSWORD_TOKEN_KEY",
            validationTime = 10.toDuration(DurationUnit.MINUTES).inWholeMilliseconds,
        )
    }
}
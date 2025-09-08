package com.poulastaa.kyoku.auth.service

import com.poulastaa.kyoku.auth.model.dto.DtoJWTConfigInfo
import com.poulastaa.kyoku.auth.model.dto.JWTTokenType
import com.poulastaa.kyoku.auth.utils.JWTToken
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import jakarta.inject.Named
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class JWTService(
    @param:Named("provideVerificationMailConfigurationsClass")
    private val verification: DtoJWTConfigInfo,
    @param:Named("provideForgotPasswordMailConfigurationsClass")
    private val forgotPassword: DtoJWTConfigInfo,
) {
    fun <T> verifyAndExtractClaim(
        token: JWTToken,
        type: JWTTokenType,
    ): T? {
        val claim = extractClaim(token, type) ?: return null

        return when (type) {
            JWTTokenType.TOKEN_VERIFICATION_MAIL -> claim[verification.claimKey] as T?
            else -> TODO("not yet implemented")
        }
    }

    private fun extractClaim(
        token: JWTToken,
        type: JWTTokenType,
    ): Claims? {
        val payload = when (type) {
            JWTTokenType.TOKEN_VERIFICATION_MAIL -> verification
            JWTTokenType.TOKEN_FORGOT_PASSWORD -> forgotPassword
            else -> TODO("not yet implemented")
        }

        return try {
            val claim = Jwts.parser().verifyWith(payload.key)
                .build()
                .parseSignedClaims(token)
                .payload

            if (claim.audience.first().equals(payload.audience).not()) return null
            if (claim.subject.equals(payload.subject).not()) return null
            if (claim.issuer.equals(payload.issuer).not()) return null

            claim
        } catch (e: Exception) {
            LoggerFactory.getLogger(this::class.java).error("Error parsing JWT token: ${e.message}")
            null
        }
    }
}
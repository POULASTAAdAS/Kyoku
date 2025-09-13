package com.poulastaa.kyoku.auth.service

import com.poulastaa.kyoku.auth.model.dto.DtoAuthenticationTokenClaim
import com.poulastaa.kyoku.auth.model.dto.DtoJWTConfigInfo
import com.poulastaa.kyoku.auth.model.dto.JWTTokenType
import com.poulastaa.kyoku.auth.model.dto.UserType
import com.poulastaa.kyoku.auth.utils.JWTToken
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.json.GsonJsonParser
import org.springframework.stereotype.Service
import java.util.*

@Service
class JWTService(
    @param:Qualifier("provideVerificationMailConfigurationsClass")
    private val verification: DtoJWTConfigInfo,
    @param:Qualifier("provideForgotPasswordMailConfigurationsClass")
    private val forgotPassword: DtoJWTConfigInfo,
    @param:Qualifier("provideAccessTokenConfigurationsClass")
    private val access: DtoJWTConfigInfo,
    @param:Qualifier("provideRefreshTokenConfigurationsClass")
    private val refresh: DtoJWTConfigInfo,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun <T> verifyAndExtractClaim(
        token: JWTToken,
        type: JWTTokenType,
    ): T? {
        val claim = extractClaim(token, type) ?: return null

        return when (type) {
            JWTTokenType.TOKEN_VERIFICATION_MAIL -> claim[verification.claimKey] as T?
            JWTTokenType.TOKEN_ACCESS,
            JWTTokenType.TOKEN_REFRESH,
                -> try {
                GsonJsonParser().parseMap(
                    claim[
                        if (type == JWTTokenType.TOKEN_ACCESS) access.claimKey
                        else refresh.claimKey
                    ].toString()
                ).let { payload ->
                    DtoAuthenticationTokenClaim(
                        email = payload["email"] as String,
                        userType = UserType.valueOf(payload["userType"] as String)
                    )
                }
            } catch (e: Exception) {
                logger.error("Error parsing JWT token: ${e.message}")
                null
            } as T?

            else -> TODO("not yet implemented")
        }
    }

    fun <T> generateToken(
        payload: T,
        type: JWTTokenType,
    ) = when (type) {
        JWTTokenType.TOKEN_ACCESS -> access
        JWTTokenType.TOKEN_REFRESH -> refresh

        JWTTokenType.TOKEN_FORGOT_PASSWORD -> TODO()
        JWTTokenType.TOKEN_SUBMIT_NEW_PASSWORD -> TODO()

        else -> throw IllegalArgumentException("Invalid token generation type")
    }.let { conf ->
        Date(System.currentTimeMillis()).let { issueTime ->
            Jwts.builder()
                .subject(conf.subject)
                .issuer(conf.issuer)
                .audience()
                .add(conf.audience)
                .and()
                .claim(conf.claimKey, payload)
                .issuedAt(issueTime)
                .expiration(Date(issueTime.time + conf.expDuration.inWholeMilliseconds))
                .signWith(conf.key, Jwts.SIG.HS256)
                .compact()
        }
    } as JWTToken

    private fun extractClaim(
        token: JWTToken,
        type: JWTTokenType,
    ): Claims? {
        val payload = when (type) {
            JWTTokenType.TOKEN_VERIFICATION_MAIL -> verification
            JWTTokenType.TOKEN_FORGOT_PASSWORD -> forgotPassword
            JWTTokenType.TOKEN_REFRESH -> refresh
            JWTTokenType.TOKEN_ACCESS -> access
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
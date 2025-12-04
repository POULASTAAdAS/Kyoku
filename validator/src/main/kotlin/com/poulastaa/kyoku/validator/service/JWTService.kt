package com.poulastaa.kyoku.validator.service

import com.poulastaa.kyoku.validator.model.dto.DtoAuthenticationTokenClaim
import com.poulastaa.kyoku.validator.model.dto.DtoJWTConfigInfo
import com.poulastaa.kyoku.validator.model.dto.UserType
import com.poulastaa.kyoku.validator.model.dto.ValidatorStatus
import com.poulastaa.kyoku.validator.utils.JWTToken
import io.jsonwebtoken.Jwts
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.json.GsonJsonParser
import org.springframework.stereotype.Service

@Service
class JWTService(
    @param:Qualifier("provideAccessTokenConfigurationsClass")
    private val access: DtoJWTConfigInfo,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun verifyAccessToken(token: JWTToken): Pair<ValidatorStatus, DtoAuthenticationTokenClaim?> {
        return try {
            val claim = Jwts.parser().verifyWith(access.key)
                .build()
                .parseSignedClaims(token)
                .payload

            if (claim.audience.first().equals(access.audience).not()) return (ValidatorStatus.TOKEN_INVALID to null)
            if (claim.subject.equals(access.subject).not()) return (ValidatorStatus.TOKEN_INVALID to null)
            if (claim.issuer.equals(access.issuer).not()) return (ValidatorStatus.TOKEN_INVALID to null)

            (ValidatorStatus.SUCCESS to GsonJsonParser().parseMap(claim[access.claimKey].toString()).let { payload ->
                DtoAuthenticationTokenClaim(
                    email = payload["email"] as String,
                    userType = UserType.valueOf(payload["userType"] as String)
                )
            })
        } catch (e: Exception) {
            logger.error("Error while verifying access token: ${e.message}")
            // todo add check for invalid and expired token

            (ValidatorStatus.TOKEN_INVALID to null)
        }
    }
}
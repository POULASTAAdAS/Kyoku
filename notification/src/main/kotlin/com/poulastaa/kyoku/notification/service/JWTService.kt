package com.poulastaa.kyoku.notification.service

import com.poulastaa.kyoku.notification.utils.Email
import com.poulastaa.kyoku.notification.utils.JWTToken
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.sql.Date
import java.util.*
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Service
class JWTService(
    @param:Value("\${jwt.secret}")
    private val secret: String,
    @param:Value("\${jwt.subject}")
    private val subject: String,
    @param:Value("\${jwt.issuer}")
    private val issuer: String,
    @param:Value("\${jwt.audience}")
    private val audience: String,
    @param:Value("\${jwt.time}")
    private val expTime: Int,
    @param:Value("\${jwt.unit}")
    private val unit: String = "MINUTES",
    @param:Value("\${jwt.claim-key}")
    private val claimKey: String,
) {
    private val UNIT = when (unit.uppercase()) {
        "MINUTES" -> expTime.minutes
        else -> 10.minutes
    }

    private val key = Keys.hmacShaKeyFor(Base64.getUrlDecoder().decode(secret))

    fun generateVerificationMailToken(
        claimValue: Email,
    ): JWTToken = Date(System.currentTimeMillis()).let { issuedAt ->
        Jwts.builder()
            .subject(subject)
            .issuer(issuer)
            .audience()
            .add(audience)
            .and()
            .claim(
                claimKey,
                claimValue
            )
            .issuedAt(issuedAt)
            .expiration(Date(issuedAt.time + UNIT.inWholeMilliseconds + 5.seconds.inWholeMilliseconds)) // adding small buffer to tackle delays
            .signWith(key, Jwts.SIG.HS256)
            .compact()
    }
}
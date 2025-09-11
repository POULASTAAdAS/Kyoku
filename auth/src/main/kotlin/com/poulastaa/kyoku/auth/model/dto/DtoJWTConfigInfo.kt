package com.poulastaa.kyoku.auth.model.dto

import io.jsonwebtoken.security.Keys
import java.util.*
import javax.crypto.SecretKey
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

data class DtoJWTConfigInfo(
    private val secret: String,
    val subject: String,
    val issuer: String,
    val audience: String,
    private val expiresIn: Int,
    private val unit: String,
    val claimKey: String,
) {
    val key: SecretKey = Keys.hmacShaKeyFor(Base64.getUrlDecoder().decode(secret))
    val expTime = when (unit.uppercase()) {
        "MINUTES" -> expiresIn.minutes
        "HOURS" -> expiresIn.hours
        "DAYS" -> expiresIn.days
        else -> throw IllegalArgumentException("Invalid unit")
    }
}

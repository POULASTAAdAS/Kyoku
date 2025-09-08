package com.poulastaa.kyoku.auth.model.dto

import io.jsonwebtoken.security.Keys
import java.util.*
import javax.crypto.SecretKey

data class DtoJWTConfigInfo(
    private val secret: String,
    val subject: String,
    val issuer: String,
    val audience: String,
    val expiresIn: Int,
    val unit: String,
    val claimKey: String,
) {
    val key: SecretKey = Keys.hmacShaKeyFor(Base64.getUrlDecoder().decode(secret))
}

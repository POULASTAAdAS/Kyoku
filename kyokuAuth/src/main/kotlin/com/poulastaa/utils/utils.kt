package com.poulastaa.utils

import com.poulastaa.utils.Constants.ACCESS_TOKEN_CLAIM_KEY
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import java.security.SecureRandom
import java.util.*

fun ApplicationCall.getClaimFromPayload(): String? =
    this.authentication.principal<JWTPrincipal>()?.payload?.getClaim(ACCESS_TOKEN_CLAIM_KEY)?.asString()


fun generateFidoChallenge(): String {
    val secureRandom = SecureRandom()
    val challengeBytes = ByteArray(32)
    secureRandom.nextBytes(challengeBytes)
    return challengeBytes.b64Encode()
}

fun ByteArray.b64Encode(): String = Base64.getUrlEncoder().encodeToString(this)
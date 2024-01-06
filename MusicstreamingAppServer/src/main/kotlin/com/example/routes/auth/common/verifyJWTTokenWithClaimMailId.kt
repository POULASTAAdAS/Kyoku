package com.example.routes.auth.common

import com.auth0.jwk.JwkProviderBuilder
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.example.invalidTokenList
import com.example.util.Constants.USED_TOKEN
import io.ktor.server.application.*
import java.security.interfaces.RSAPublicKey
import java.util.concurrent.TimeUnit

fun String.verifyJWTTokenWithClaimMailId(env: ApplicationEnvironment): String? {
    val issuer = env.config.property("jwt.issuer").getString()
    val audience = env.config.property("jwt.audience").getString()

    val jwkProvider = JwkProviderBuilder(issuer)
        .cached(1, 5, TimeUnit.MINUTES)
        .rateLimited(3, 1, TimeUnit.MINUTES)
        .build()

    val publicKey = jwkProvider.get("6f8856ed-9189-488f-9011-0ff4b6c08edc").publicKey
    val algorithm = Algorithm.RSA256(publicKey as RSAPublicKey, null)

    if (invalidTokenList.contains(this)) return USED_TOKEN

    return try {
        invalidTokenList.add(this)
        JWT.require(algorithm)
            .withAudience(audience)
            .withIssuer(issuer)
            .build()
            .verify(this).getClaim("email").asString()
    } catch (e: JWTVerificationException) {
        null
    } catch (e: Exception) {
        null
    }
}
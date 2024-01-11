package com.example.util

import com.auth0.jwk.JwkProviderBuilder
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*

fun String.generateJWTTokenWithClaimMailId(
    env: ApplicationEnvironment,
    time: Long = 300000L
): String {
    val issuer = env.config.property("jwt.issuer").getString()
    val audience = env.config.property("jwt.audience").getString()
    val privateKeyString = env.config.property("jwt.privateKey").getString()

    val jwkProvider = JwkProviderBuilder(issuer)
        .build()

    val publicKey = jwkProvider.get("6f8856ed-9189-488f-9011-0ff4b6c08edc").publicKey
    val keySpecPKCS8 = PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString))
    val privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpecPKCS8)

    return JWT.create()
        .withSubject("Authentication")
        .withAudience(audience)
        .withIssuer(issuer)
        .withClaim("email", this)
        .withExpiresAt(Date(System.currentTimeMillis() + time))
        .sign(Algorithm.RSA256(publicKey as RSAPublicKey, privateKey as RSAPrivateKey))
}
package com.poulastaa.auth.data.repository

import com.auth0.jwk.JwkProvider
import com.auth0.jwk.JwkProviderBuilder
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.poulastaa.core.domain.repository.Email
import com.poulastaa.core.domain.repository.JWTRepository
import com.poulastaa.core.domain.repository.JWTToken
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*

class JWTRepositoryService(
    private val issuer: String,
    private val audience: String,
    val privateKeyPayload: String,
) : JWTRepository {
    private val privateKey: PrivateKey = KeyFactory
        .getInstance("RSA")
        .generatePrivate(PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyPayload)))

    private val jwKProvider: JwkProvider = JwkProviderBuilder(issuer).build()


    override fun generateToken(
        email: String,
        type: JWTRepository.TokenType,
    ): JWTToken {
        val algorithm: Algorithm = Algorithm.RSA256(getPublicKey(), privateKey as RSAPrivateKey)

        return JWT.create()
            .withSubject(type.sub)
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim(type.claimKey, email)
            .withExpiresAt(Date(System.currentTimeMillis() + type.validationTime))
            .sign(algorithm)
    }

    override fun verifyToken(
        token: String,
        type: JWTRepository.TokenType,
    ): Email? = try {
        getJWTVerifier()
            .verify(token)
            .getClaim(type.claimKey)
            .asString()
    } catch (_: Exception) {
        null
    }

    private fun getPublicKey(): RSAPublicKey {
        return try {
            val jwk = jwKProvider.get("6f8856ed-9189-488f-9011-0ff4b6c08edc")
            jwk.publicKey as RSAPublicKey
        } catch (e: Exception) {
            throw IllegalStateException("Failed to retrieve public key from JWK provider", e)
        }
    }

    private fun getJWTVerifier(): JWTVerifier {
        val publicKey = jwKProvider.get("6f8856ed-9189-488f-9011-0ff4b6c08edc").publicKey
        val algorithm = Algorithm.RSA256(publicKey as RSAPublicKey, null)

        return JWT.require(algorithm)
            .withAudience(audience)
            .withIssuer(issuer)
            .build()
    }
}
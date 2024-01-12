package com.example.data.repository.jwt

import com.auth0.jwk.JwkProviderBuilder
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.example.data.model.auth.stat.UpdateEmailVerificationStatus
import com.example.domain.repository.jwt.JWTRepository
import com.example.invalidTokenList
import io.ktor.server.application.*
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*


class JWTRepositoryImpl(
    private val application: Application
) : JWTRepository {
    private val issuer = getConfigProperty("jwt.issuer")
    private val audience = getConfigProperty("jwt.audience")
    private val privateKeyString = getConfigProperty("jwt.privateKey")

    private val keySpecPKCS8 = PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString))
    private val privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpecPKCS8)

    private val realm = getConfigProperty("jwt.realm")

    override fun getIssuer() = issuer
    override fun getRealm() = realm

    override fun generateAccessToken(
        sub: String,
        email: String,
        claimName: String,
        validationTime: Long
    ): String = generateJWTToken(
        sub = sub,
        claimName = claimName,
        email = email,
        validationTime = validationTime
    )

    override fun generateRefreshToken(
        sub: String,
        email: String,
        claimName: String,
        validationTime: Long
    ): String = generateJWTToken(
        sub = sub,
        claimName = claimName,
        email = email,
        validationTime = validationTime
    )

    override fun generateVerificationMailToken(
        sub: String,
        email: String,
        claimName: String,
        validationTime: Long
    ): String = generateJWTToken(
        sub = sub,
        claimName = claimName,
        email = email,
        validationTime = validationTime
    )

    override fun generateForgotPasswordMailToken(
        sub: String,
        email: String,
        claimName: String,
        validationTime: Long
    ): String = generateJWTToken(
        sub = sub,
        claimName = claimName,
        email = email,
        validationTime = validationTime
    )

    override fun verifyJWTToken(
        token: String,
        claim: String
    ): String? {
        if (invalidTokenList.contains(token)) return UpdateEmailVerificationStatus.TOKEN_USED.name

        return try {
            getJWTVerifier()
                .verify(token).getClaim(claim).asString()
        } catch (e: Exception) {
            null
        }
    }


    private fun generateJWTToken(
        sub: String,
        claimName: String,
        email: String,
        validationTime: Long
    ): String {
        val publicKey = provideJWKProvider().get("6f8856ed-9189-488f-9011-0ff4b6c08edc").publicKey

        return JWT.create()
            .withSubject(sub)
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim(claimName, email)
            .withExpiresAt(Date(System.currentTimeMillis() + validationTime))
            .sign(Algorithm.RSA256(publicKey as RSAPublicKey, privateKey as RSAPrivateKey))
    }

    private fun getJWTVerifier(): JWTVerifier {
        val publicKey = provideJWKProvider().get("6f8856ed-9189-488f-9011-0ff4b6c08edc").publicKey
        val algorithm = Algorithm.RSA256(publicKey as RSAPublicKey, null)

        return JWT.require(algorithm)
            .withAudience(audience)
            .withIssuer(issuer)
            .build()
    }

    private fun provideJWKProvider() = JwkProviderBuilder(issuer)
        .build()

    private fun getConfigProperty(name: String) = application.environment.config
        .property(name).getString()
}
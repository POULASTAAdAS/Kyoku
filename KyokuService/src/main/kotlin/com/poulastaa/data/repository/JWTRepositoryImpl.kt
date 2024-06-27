package com.poulastaa.data.repository

import com.auth0.jwk.JwkProviderBuilder
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.poulastaa.domain.repository.JWTRepository
import io.ktor.server.application.*
import java.security.interfaces.RSAPublicKey

class JWTRepositoryImpl(
    private val application: Application,
) : JWTRepository {
    private val issuer = getConfigProperty("jwt.issuer")
    private val audience = getConfigProperty("jwt.audience")
    private val privateKeyString = getConfigProperty("jwt.privateKey")

    private val realm = getConfigProperty("jwt.realm")

    override fun getIssuer() = issuer
    override fun getRealm() = realm

    override fun verifyJWTToken(
        token: String,
        claim: String,
    ): String? {
        return try {
            getJWTVerifier()
                .verify(token).getClaim(claim).asString()
        } catch (e: Exception) {
            null
        }
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
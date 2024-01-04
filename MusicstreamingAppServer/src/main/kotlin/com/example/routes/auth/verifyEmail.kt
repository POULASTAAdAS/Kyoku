package com.example.routes.auth

import com.auth0.jwk.JwkProviderBuilder
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.example.data.model.EndPoints
import com.example.domain.repository.user.EmailAuthUserRepository
import com.example.util.UpdateEmailVerificationStatus
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.security.interfaces.RSAPublicKey
import java.util.concurrent.TimeUnit


fun Route.verifyEmail(emailAuthUser: EmailAuthUserRepository) {
    route(EndPoints.VerifyEmail.route) {
        get {
            val token = call.parameters["token"]

            val issuer = call.application.environment.config.property("jwt.issuer").getString()
            val audience = call.application.environment.config.property("jwt.audience").getString()

            val jwkProvider = JwkProviderBuilder(issuer)
                .cached(1, 5, TimeUnit.MINUTES)
                .rateLimited(3, 1, TimeUnit.MINUTES)
                .build()

            val publicKey = jwkProvider.get("6f8856ed-9189-488f-9011-0ff4b6c08edc").publicKey

            val algorithm = Algorithm.RSA256(publicKey as RSAPublicKey, null)

            val email = try {
                JWT.require(algorithm)
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .build()
                    .verify(token).getClaim("email").asString()
            } catch (e: JWTVerificationException) {
                null
            }

            email?.let {
                when (emailAuthUser.updateVerificationStatus(it)) {
                    UpdateEmailVerificationStatus.DONE -> {
                        call.respond(
                            message = "email verified",
                            status = HttpStatusCode.OK
                        )
                    }

                    UpdateEmailVerificationStatus.ALREADY_VERIFIED -> {
                        call.respond(
                            message = "email already verified",
                            status = HttpStatusCode.OK
                        )
                    }

                    UpdateEmailVerificationStatus.SOMETHING_WENT_WRONG -> {
                        call.respond(
                            message = "something went wrong",
                            status = HttpStatusCode.InternalServerError
                        )
                    }
                }
                return@get
            }

            call.respond(
                message = "This verification link is no more active",
                status = HttpStatusCode.Forbidden
            )
        }
    }
}
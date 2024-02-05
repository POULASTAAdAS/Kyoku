package com.poulastaa.plugins

import com.auth0.jwk.JwkProviderBuilder
import com.poulastaa.data.model.EndPoints
import com.poulastaa.data.model.auth.GoogleUserSession
import com.poulastaa.data.model.auth.PasskeyUserSession
import com.poulastaa.domain.repository.jwt.JWTRepository
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import org.koin.ktor.ext.inject

fun Application.configureSecurity() {
    val jwtRepository: JWTRepository by inject()

    val myRealm = jwtRepository.getRealm()
    val issuer = jwtRepository.getIssuer()

    install(Authentication) {
        jwt("jwt-auth") {
            realm = myRealm

            verifier(
                JwkProviderBuilder(issuer)
                    .build(), issuer
            )

            validate {
                JWTPrincipal(it.payload)
            }

            challenge { _, _ ->
                call.respondRedirect(EndPoints.UnAuthorised.route)
            }
        }

        session<GoogleUserSession>("google-auth") {
            validate { it }

            challenge { call.resolveResource(EndPoints.UnAuthorised.route) }
        }

        session<PasskeyUserSession>("passkey-auth") {
            validate { it }

            challenge { call.resolveResource(EndPoints.UnAuthorised.route) }
        }
    }
}
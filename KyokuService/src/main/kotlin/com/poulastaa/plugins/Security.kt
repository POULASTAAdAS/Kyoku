package com.poulastaa.plugins

import com.auth0.jwk.JwkProviderBuilder
import com.poulastaa.domain.model.EndPoints
import com.poulastaa.domain.model.UserSession
import com.poulastaa.domain.repository.JWTRepository
import com.poulastaa.utils.Constants.SECURITY_LIST
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
        jwt(SECURITY_LIST[0]) {
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

        session<UserSession>(SECURITY_LIST[1]) {
            validate { it }

            challenge { call.respondRedirect(EndPoints.UnAuthorised.route) }
        }
    }
}
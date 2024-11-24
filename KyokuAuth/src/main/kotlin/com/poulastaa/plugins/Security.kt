package com.poulastaa.plugins

import com.auth0.jwk.JwkProviderBuilder
import com.poulastaa.data.model.EndPoints
import com.poulastaa.data.model.session.UserSession
import com.poulastaa.domain.repository.JWTRepository
import com.poulastaa.utils.Constants
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
        jwt(Constants.SECURITY_LIST[0]) {
            realm = myRealm

            verifier(
                jwkProvider = JwkProviderBuilder(issuer).build(),
                issuer = issuer
            )

            validate {
                JWTPrincipal(it.payload)
            }

            challenge { _, _ ->
                call.respondRedirect(EndPoints.UnAuthorised.route)
            }
        }

        session<UserSession>(Constants.SECURITY_LIST[1]) {
            validate { it }

            challenge { call.resolveResource(EndPoints.UnAuthorised.route) }
        }
    }
}

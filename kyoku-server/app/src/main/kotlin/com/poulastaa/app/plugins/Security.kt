package com.poulastaa.app.plugins

import com.auth0.jwk.JwkProviderBuilder
import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.domain.utils.Constants.SECURITY_LIST
import com.poulastaa.core.network.model.UserSession
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureSecurity(
    realm: String,
    issuer: String,
) {
    install(Authentication) {
        jwt(name = SECURITY_LIST[0]) {
            this.realm = realm

            verifier(
                JwkProviderBuilder(issuer)
                    .build(), issuer
            )

            validate {
                JWTPrincipal(it.payload)
            }

            challenge { _, _ ->
                call.respondRedirect(EndPoints.UnAuthorized.route)
            }
        }

        session<UserSession>(SECURITY_LIST[1]) {
            validate { it }

            challenge { call.respondRedirect(EndPoints.UnAuthorized.route) }
        }
    }
}
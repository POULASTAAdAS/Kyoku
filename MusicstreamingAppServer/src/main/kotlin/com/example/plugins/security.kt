package com.example.plugins

import com.auth0.jwk.JwkProviderBuilder
import com.example.data.model.EndPoints
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureSecurity() {
    val myRealm = environment.config.property("jwt.realm").getString()
    val issuer = environment.config.property("jwt.issuer").getString()

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
    }
}
package com.example.plugins

import com.auth0.jwk.JwkProviderBuilder
import com.example.data.model.EndPoints
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import java.util.concurrent.TimeUnit

fun Application.configureSecurity() {
    val myRealm = environment.config.property("jwt.realm").getString()
    val issuer = environment.config.property("jwt.issuer").getString()

    install(Authentication) {
        jwt("jwt-auth") {
            realm = myRealm

            verifier(
                JwkProviderBuilder(issuer)
                    .cached(10, 24, TimeUnit.HOURS)
                    .rateLimited(10, 1, TimeUnit.MINUTES)
                    .build(), issuer
            ) {
                acceptLeeway(3L)
            }

            validate {
                JWTPrincipal(it.payload)
            }

            challenge { _, _ ->
                call.respondRedirect(EndPoints.UnAuthorised.route)
            }
        }
    }
}
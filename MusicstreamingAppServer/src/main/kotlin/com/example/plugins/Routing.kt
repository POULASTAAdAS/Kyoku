package com.example.plugins

import com.example.domain.repository.user.EmailAuthUserRepository
import com.example.routes.auth.verifyEmail
import com.example.routes.auth.createAuthRoute
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.get
import java.io.File

fun Application.configureRouting() {
    val emailAuthUser: EmailAuthUserRepository = get()

    routing {
        createAuthRoute(emailAuthUser = emailAuthUser)
        verifyEmail(emailAuthUser = emailAuthUser)

        static(".well-known") {
            staticRootFolder = File("certs")
            file("jwks.json")
        }
    }
}
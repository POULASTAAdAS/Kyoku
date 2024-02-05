package com.poulastaa.plugins

import com.poulastaa.data.model.auth.google.GoogleUserSession
import com.poulastaa.data.model.auth.passkey.PasskeyUserSession
import com.poulastaa.domain.repository.UserServiceRepository
import com.poulastaa.routes.auth.*
import com.poulastaa.routes.getUserProfilePic
import com.poulastaa.routes.unauthorised
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.koin.ktor.ext.inject
import java.io.File

fun Application.configureRouting() {
    val userService: UserServiceRepository by inject()
    routing {
        sessionInterceptor()

        authRoute(userService)

        createPasskeyUser(userService)
        getPasskeyUser(userService)

        verifyEmail(userService)
        emailVerificationCheck(userService)

        forgotPassword(userService)
        resetPassword(userService)

        getUserProfilePic(userService)

        refreshToken(userService)

        unauthorised()

        staticFiles(
            remotePath = ".well-known",
            dir = File("certs")
        )
    }
}

private fun Route.sessionInterceptor() {
    intercept(ApplicationCallPipeline.Call) {
        call.sessions.get<GoogleUserSession>()?.let {
            call.sessions.set(it)
        }

        call.sessions.get<PasskeyUserSession>()?.let {
            call.sessions.set(it)
        }
    }
}

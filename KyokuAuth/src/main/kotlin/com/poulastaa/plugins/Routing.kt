package com.poulastaa.plugins

import com.poulastaa.data.model.session.UserSession
import com.poulastaa.domain.repository.ServiceRepository
import com.poulastaa.routes.auth.auth
import com.poulastaa.routes.unAuthorised
import com.poulastaa.routes.verify_mail.logInEmailVerificationCheck
import com.poulastaa.routes.verify_mail.signUpEmailVerificationCheck
import com.poulastaa.routes.verify_mail.verifyLogInMail
import com.poulastaa.routes.verify_mail.verifySignUpMail
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.koin.ktor.ext.inject
import java.io.File

fun Application.configureRouting() {
    val service: ServiceRepository by inject()

    routing {
        sessionInterceptor()

        auth(service)

        verifySignUpMail(service)
        verifyLogInMail(service)

        signUpEmailVerificationCheck(service)
        logInEmailVerificationCheck(service)

        unAuthorised()

        staticFiles(
            remotePath = ".well-known",
            dir = File("certs")
        )
    }
}

private fun Route.sessionInterceptor() {
    intercept(ApplicationCallPipeline.Call) {
        call.sessions.get<UserSession>()?.let {
            call.sessions.set(it)
        }
    }
}
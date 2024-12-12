package com.poulastaa.auth.network.routes

import com.poulastaa.auth.domain.model.EmailVerificationStatusDto
import com.poulastaa.auth.domain.repository.AuthRepository
import com.poulastaa.auth.network.routes.utils.responseVerificationMailHtml
import com.poulastaa.core.domain.model.Endpoints
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.verifyEmail(
    repo: AuthRepository,
) {
    route(Endpoints.VerifyEmail.route) {
        get {
            val token = call.parameters["token"] ?: return@get call.respondRedirect(Endpoints.UnAuthorized.route)
            val status = repo.verifyEmail(token)

            val response = when (status) {
                EmailVerificationStatusDto.VERIFIED -> responseVerificationMailHtml(
                    headingColor = "#309786",
                    heading = "Authentication Successful",
                    title = "Authentication Successful",
                    content1 = "Email Verification Successful.",
                    content2 = "Please Navigate back to app to continue."
                )

                EmailVerificationStatusDto.TOKEN_USED -> responseVerificationMailHtml(
                    headingColor = "#e65353",
                    heading = "Authentication Failed",
                    title = "Link Already Used",
                    content1 = "The Verification link has been used.",
                    content2 = "Please try again."
                )

                EmailVerificationStatusDto.USER_NOT_FOUND -> responseVerificationMailHtml(
                    headingColor = "#e65353",
                    heading = "Authentication Failed",
                    title = "No User Found",
                    content1 = "No User found on This Email.",
                    content2 = "Please SignUp. First"
                )

                EmailVerificationStatusDto.SERVER_ERROR -> responseVerificationMailHtml(
                    headingColor = "#e65353",
                    heading = "Authentication Failed",
                    title = "Error Occurred",
                    content1 = "Opp's Something went wrong.",
                    content2 = "Please try again."
                )
            }

            call.respondText(
                text = response,
                contentType = ContentType.Text.Html,
            )
        }
    }
}
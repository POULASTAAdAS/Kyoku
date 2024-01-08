package com.example.routes.auth

import com.example.data.model.EndPoints
import com.example.data.model.auth.SendForgotPasswordMail
import com.example.data.model.auth.SendVerificationMailStatus
import com.example.domain.repository.user_db.EmailAuthUserRepository
import com.example.util.Constants.BASE_URL
import com.example.util.generateJWTTokenWithClaimMailId
import com.example.util.sendEmail
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.forgotPassword(
    emailAuthUser: EmailAuthUserRepository
) {
    route(EndPoints.ForgotPassword.route) {
        get {
            val email = call.parameters["email"]

            if (email == null) {
                call.respondRedirect(EndPoints.UnAuthorised.route)

                return@get
            }

            val result = emailAuthUser.sendForgotPasswordMail(email = email)

            when (result.status) {
                SendVerificationMailStatus.USER_EXISTS -> {
                    val content = (
                            (
                                    "<html>"
                                            + "<body>"
                                            + "<h1>Do not share this email</h1>"
                                            + "<p>Click the following link to reset your password:</p>"
                                            + "<a href=\"${BASE_URL + EndPoints.ResetPassword.route}?token=" +
                                            email.trim().generateJWTTokenWithClaimMailId(call.application.environment)
                                    ) + "\">Reset Password</a>" // todo add js and textField
                                    + "</body>"
                                    + "</html>"
                            )

                    if (
                        sendEmail(
                            to = email,
                            subject = "Reset password mail",
                            content = content
                        )
                    ) call.respond(
                        message = result,
                        status = HttpStatusCode.OK
                    ) else call.respond(
                        message = SendForgotPasswordMail(
                            status = SendVerificationMailStatus.SOMETHING_WENT_WRONG
                        ),
                        status = HttpStatusCode.Forbidden
                    )
                }

                SendVerificationMailStatus.USER_NOT_FOUND -> {
                    call.respond(
                        message = result,
                        status = HttpStatusCode.Forbidden
                    )
                }

                SendVerificationMailStatus.SOMETHING_WENT_WRONG -> {
                    call.respond(
                        message = result,
                        status = HttpStatusCode.Forbidden
                    )
                }
            }
        }
    }
}
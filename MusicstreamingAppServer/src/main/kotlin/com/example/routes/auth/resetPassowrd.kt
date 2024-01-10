package com.example.routes.auth

import com.example.data.model.EndPoints
import com.example.domain.repository.user_db.EmailAuthUserRepository
import com.example.data.model.auth.stat.PasswordResetStatus
import com.example.util.verifyJWTTokenWithClaimMailId
import com.example.util.Constants.USED_TOKEN
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.resetPassword(
    emailAuthUser: EmailAuthUserRepository
) {
    route(EndPoints.ResetPassword.route) {
        get {
            val token = call.parameters["token"]
            val password = call.parameters["password"]


            if (password == null || token == null) {
                call.respond(
                    message = "invalid request",
                    status = HttpStatusCode.Forbidden
                )

                return@get
            }

            token.verifyJWTTokenWithClaimMailId(call.application.environment)?.let { result ->
                if (result == USED_TOKEN) {
                    call.respond(
                        message = "This link has been already used",
                        status = HttpStatusCode.OK
                    )

                    return@get
                }

                when (
                    emailAuthUser.passwordReset(
                        email = result,
                        password = password
                    )
                ) {
                    PasswordResetStatus.SUCCESSFUL -> {
                        call.respond( // todo return proper html
                            message = "Password Reset successfully go back to app to log in again",
                            status = HttpStatusCode.OK
                        )
                    }

                    PasswordResetStatus.SAME_AS_OLD_PASSWORD -> {
                        call.respond( // todo return proper html
                            message = "same as old password",
                            status = HttpStatusCode.OK
                        )
                    }

                    PasswordResetStatus.SOMETHING_WENT_WRONG -> {
                        call.respond( // todo return proper html
                            message = "Something went wrong please try again",
                            status = HttpStatusCode.InternalServerError
                        )
                    }
                }

                return@get
            }



            call.respond(// todo return proper html
                message = "This verification link is no more active",
                status = HttpStatusCode.Forbidden
            )
        }
    }
}
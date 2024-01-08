package com.example.routes.auth

import com.example.data.model.EndPoints
import com.example.domain.repository.user_db.EmailAuthUserRepository
import com.example.data.model.auth.UpdateEmailVerificationStatus
import com.example.util.verifyJWTTokenWithClaimMailId
import com.example.util.Constants.USED_TOKEN
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.verifyEmail(emailAuthUser: EmailAuthUserRepository) {
    route(EndPoints.VerifyEmail.route) {
        get {
            val token = call.parameters["token"]

            if (token == null) {
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

                when (emailAuthUser.updateVerificationStatus(result)) {
                    UpdateEmailVerificationStatus.DONE -> {
                        call.respond( // todo return proper html
                            message = "email verified",
                            status = HttpStatusCode.OK
                        )
                    }

                    UpdateEmailVerificationStatus.ALREADY_VERIFIED -> {
                        call.respond(// todo return proper html
                            message = "email already verified",
                            status = HttpStatusCode.OK
                        )
                    }

                    UpdateEmailVerificationStatus.SOMETHING_WENT_WRONG -> {
                        call.respond(// todo return proper html
                            message = "something went wrong",
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
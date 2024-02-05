package com.poulastaa.routes.auth.common

import com.poulastaa.data.model.auth.EmailLoginReq
import com.poulastaa.data.model.auth.jwt.EmailLoginResponse
import com.poulastaa.data.model.auth.jwt.EmailLoginStatus
import com.poulastaa.domain.repository.UserServiceRepository
import com.poulastaa.utils.verifyEmailIdWithApi
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*

suspend fun PipelineContext<Unit, ApplicationCall>.handleEmailLogin(
    emailLoginReq: EmailLoginReq,
    userService: UserServiceRepository,
) {
    if (false) // reduce unnecessary api call while developing
        verifyEmailIdWithApi(
            email = emailLoginReq.email,
        ).let {
            if (!it) {
                call.respond(
                    message = EmailLoginResponse(
                        status = EmailLoginStatus.EMAIL_NOT_VALID
                    ),
                    status = HttpStatusCode.BadRequest
                )
                return
            }
        }

    val result = userService.getEmailUser(
        email = emailLoginReq.email.trim(),
        password = emailLoginReq.password.trim()
    )

    when (result.status) {
        EmailLoginStatus.USER_PASS_MATCHED -> {
            call.respond(
                message = result,
                status = HttpStatusCode.OK
            )
        }

        EmailLoginStatus.PASSWORD_DOES_NOT_MATCH -> {
            call.respond(
                message = result,
                status = HttpStatusCode.BadRequest
            )
        }

        EmailLoginStatus.EMAIL_NOT_VERIFIED -> {
            call.respond(
                message = result,
                status = HttpStatusCode.BadRequest
            )
        }

        EmailLoginStatus.USER_DOES_NOT_EXISTS -> {
            call.respond(
                message = result,
                status = HttpStatusCode.Forbidden
            )
        }

        EmailLoginStatus.SOMETHING_WENT_WRONG -> {
            call.respond(
                message = result,
                status = HttpStatusCode.InternalServerError
            )
        }

        else -> Unit
    }
}
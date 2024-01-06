package com.example.routes.auth.common

import com.example.data.model.EmailLoginReq
import com.example.domain.repository.user.EmailAuthUserRepository
import com.example.util.Constants.JWT_TOKEN_DEFAULT_TIME
import com.example.util.verifyEmailIdWithApi
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*

suspend fun PipelineContext<Unit, ApplicationCall>.handleEmailLogin(
    emailLoginReq: EmailLoginReq,
    emailAuthUser: EmailAuthUserRepository
) {
    if (false) // reduce unnecessary api call while developing
        verifyEmailIdWithApi(
            email = emailLoginReq.email,
        ).let {
            if (!it) {
                call.respond(
                    message = "not a valid email",
                    status = HttpStatusCode.BadRequest
                )
                return
            }
        }

    val result = emailAuthUser.loginUser(
        email = emailLoginReq.email.trim(),
        password = emailLoginReq.password.trim(),
        token = emailLoginReq.email.trim().generateJWTTokenWithClaimMailId(
            env = call.application.environment,
            time = JWT_TOKEN_DEFAULT_TIME
        )
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
                status = HttpStatusCode.OK
            )
        }

        EmailLoginStatus.EMAIL_NOT_VERIFIED -> {
            call.respond(
                message = result,
                status = HttpStatusCode.OK
            )
        }

        EmailLoginStatus.USER_DOES_NOT_EXISTS -> {
            call.respond(
                message = result,
                status = HttpStatusCode.OK
            )
        }

        EmailLoginStatus.SOMETHING_WENT_WRONG -> {
            call.respond(
                message = result,
                status = HttpStatusCode.InternalServerError
            )
        }
    }
}
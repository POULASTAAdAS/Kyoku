package com.poulastaa.routes.auth.components

import com.poulastaa.data.model.auth.req.EmailSignUpReq
import com.poulastaa.data.model.auth.response.UserAuthStatus
import com.poulastaa.domain.repository.ServiceRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*

suspend fun PipelineContext<Unit, ApplicationCall>.handleEmailSingUp(
    service: ServiceRepository,
    req: EmailSignUpReq,
) {
    val response = service.createEmailUser(req)

    when (response.status) {
        UserAuthStatus.CREATED -> return call.respond(
            message = response,
            status = HttpStatusCode.Created
        )

        UserAuthStatus.USER_FOUND_HOME,
        UserAuthStatus.USER_FOUND_SET_ARTIST,
        UserAuthStatus.USER_FOUND_SET_GENRE,
        UserAuthStatus.USER_FOUND_STORE_B_DATE,
        -> call.respond(
            message = response,
            status = HttpStatusCode.Conflict
        )

        UserAuthStatus.EMAIL_NOT_VALID -> return call.respond(
            message = response,
            status = HttpStatusCode.OK
        )

        UserAuthStatus.CONFLICT -> return call.respond(
            message = response,
            status = HttpStatusCode.Conflict
        )

        else -> return call.respond(
            message = response,
            status = HttpStatusCode.InternalServerError
        )
    }
}
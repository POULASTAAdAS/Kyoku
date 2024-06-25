package com.poulastaa.routes.auth.components

import com.poulastaa.data.model.EndPoints
import com.poulastaa.data.model.auth.req.EmailLogInReq
import com.poulastaa.data.model.auth.response.UserAuthStatus
import com.poulastaa.domain.repository.ServiceRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*

suspend fun PipelineContext<Unit, ApplicationCall>.handleEmailLogin(
    service: ServiceRepository,
    req: EmailLogInReq,
) {
    val response = service.loginEmailUser(req)

    when (response.status) {
        UserAuthStatus.USER_FOUND_HOME,
        UserAuthStatus.USER_FOUND_SET_ARTIST,
        UserAuthStatus.USER_FOUND_SET_GENRE,
        UserAuthStatus.USER_FOUND_STORE_B_DATE,
        -> call.respond(
            message = response,
            status = HttpStatusCode.OK
        )


        UserAuthStatus.PASSWORD_DOES_NOT_MATCH -> call.respond(
            message = response,
            status = HttpStatusCode.Forbidden
        )

        UserAuthStatus.USER_NOT_FOUND -> call.respond(
            message = response,
            status = HttpStatusCode.NotFound
        )

        UserAuthStatus.EMAIL_NOT_VALID -> call.respond(
            message = response,
            status = HttpStatusCode.BadRequest
        )

        UserAuthStatus.EMAIL_NOT_VERIFIED -> call.respond(
            message = response,
            status = HttpStatusCode.NotAcceptable
        )

        UserAuthStatus.SOMETHING_WENT_WRONG -> call.respond(
            message = response,
            status = HttpStatusCode.InternalServerError
        )

        else -> call.respondRedirect(EndPoints.UnAuthorised.route)
    }
}
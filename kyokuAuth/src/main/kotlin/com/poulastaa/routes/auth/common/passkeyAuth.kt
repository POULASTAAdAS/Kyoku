package com.poulastaa.routes.auth.common

import com.poulastaa.data.model.auth.PasskeyAuthReq
import com.poulastaa.data.model.auth.passkey.CreatePasskeyJson
import com.poulastaa.data.model.auth.passkey.GetPasskeyJson
import com.poulastaa.domain.repository.UserServiceRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*

suspend fun PipelineContext<Unit, ApplicationCall>.handlePasskeyAuth(
    passkeyAuthReq: PasskeyAuthReq,
    userService: UserServiceRepository
) {
    val response = userService.getPasskeyJsonResponse(
        email = passkeyAuthReq.email,
        displayName = passkeyAuthReq.displayName
    )

    call.respond(
        message = response,
        status = HttpStatusCode.OK
    )
}
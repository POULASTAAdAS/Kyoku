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
    val user = userService.findUserByEmail(email = passkeyAuthReq.email)

    user?.let {
        call.respond(
            message = GetPasskeyJson(
                allowCredentials = listOf(
                    GetPasskeyJson.AllowCredentials(
                        id = it.userId,
                        transports = listOf(),
                        type = "public-key"
                    )
                )
            ),
            status = HttpStatusCode.OK
        )

        return
    }

    call.respond(
        message = CreatePasskeyJson(
            user = CreatePasskeyJson.User(
                name = passkeyAuthReq.email,
                displayName = passkeyAuthReq.displayName
            )
        ),
        status = HttpStatusCode.OK
    )
}
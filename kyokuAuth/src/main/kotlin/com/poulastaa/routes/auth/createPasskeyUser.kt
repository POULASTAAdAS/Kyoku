package com.poulastaa.routes.auth

import com.poulastaa.data.model.EndPoints
import com.poulastaa.data.model.auth.passkey.CreatePasskeyUserReq
import com.poulastaa.data.model.auth.passkey.PasskeyUserSession
import com.poulastaa.data.model.auth.UserCreationStatus
import com.poulastaa.data.model.auth.passkey.PasskeyAuthResponse
import com.poulastaa.domain.repository.UserServiceRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Route.createPasskeyUser(
    userService: UserServiceRepository
) {
    route(EndPoints.CreatePasskeyUser.route) {
        post {
            val userReq = call.receiveNullable<CreatePasskeyUserReq>() ?: return@post call.respond(
                message = PasskeyAuthResponse(
                    status = UserCreationStatus.USER_NOT_FOUND
                ),
                status = HttpStatusCode.BadRequest
            )

            val response = userService.createPasskeyUser(
                userName = userReq.userName,
                email = userReq.email,
                userId = userReq.id
            )

            when (response.status) {
                UserCreationStatus.CREATED -> {
                    userReq.setSession(call)

                    call.respond(
                        message = response,
                        status = HttpStatusCode.OK
                    )
                }

                UserCreationStatus.CONFLICT -> {
                    userReq.setSession(call)

                    call.respond(
                        message = response,
                        status = HttpStatusCode.OK
                    )
                }

                else -> {
                    call.respond(
                        message = PasskeyAuthResponse(
                            status = UserCreationStatus.SOMETHING_WENT_WRONG
                        ),
                        status = HttpStatusCode.NotFound
                    )
                }
            }
        }
    }
}

private fun CreatePasskeyUserReq.setSession(call: ApplicationCall) {
    call.sessions.set(
        PasskeyUserSession(
            id = this.id,
            userName = this.userName
        )
    )
}
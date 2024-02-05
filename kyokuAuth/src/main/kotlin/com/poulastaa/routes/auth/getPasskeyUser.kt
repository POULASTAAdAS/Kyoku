package com.poulastaa.routes.auth

import com.poulastaa.data.model.EndPoints
import com.poulastaa.data.model.auth.UserCreationStatus
import com.poulastaa.data.model.auth.passkey.GetPasskeyUserReq
import com.poulastaa.data.model.auth.passkey.PasskeyAuthResponse
import com.poulastaa.data.model.auth.passkey.PasskeyUserSession
import com.poulastaa.domain.repository.UserServiceRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Route.getPasskeyUser(
    userService: UserServiceRepository
) {
    route(EndPoints.GetPasskeyUser.route) {
        post {
            val userReq = call.receiveNullable<GetPasskeyUserReq>() ?: return@post call.respond(
                message = PasskeyAuthResponse(
                    status = UserCreationStatus.USER_NOT_FOUND
                ),
                status = HttpStatusCode.BadRequest
            )

            val response = userService.getPasskeyUser(userId = userReq.id)


            if (response.status == UserCreationStatus.CONFLICT)
                response.setSession(call, userReq.id)

            call.respond(
                message = response,
                status = HttpStatusCode.OK
            )
        }
    }
}

private fun PasskeyAuthResponse.setSession(
    call: ApplicationCall,
    id: String
) {
    call.sessions.set(
        PasskeyUserSession(
            id = id,
            userName = this.user.userName
        )
    )
}
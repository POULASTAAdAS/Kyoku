package com.poulastaa.auth.network.routes

import com.poulastaa.auth.domain.repository.AuthRepository
import com.poulastaa.auth.network.model.PasskeyRequest
import com.poulastaa.auth.network.model.SignUpPasskeyResponse
import com.poulastaa.auth.network.model.SignUpPasskeyResponse.User
import com.poulastaa.core.domain.model.EndPoints
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getPasskey(repo: AuthRepository) {
    route(EndPoints.GetPasskey.route) {
        get {
            val req = call.receiveNullable<PasskeyRequest>()
                ?: return@get call.respondRedirect(EndPoints.UnAuthorized.route)

            when (val user = repo.getPasskeyUser(req.email)) {
                null -> call.respond(
                    message = SignUpPasskeyResponse(
                        user = User(
                            name = req.email,
                            displayName = req.email.split("@").first()
                        )
                    )
                )

                else -> TODO()
            }
        }
    }
}

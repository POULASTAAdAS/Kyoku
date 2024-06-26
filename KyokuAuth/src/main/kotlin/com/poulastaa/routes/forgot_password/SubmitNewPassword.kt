package com.poulastaa.routes.forgot_password

import com.poulastaa.data.model.EndPoints
import com.poulastaa.data.model.auth.req.ResetPasswordReq
import com.poulastaa.data.model.auth.response.ResetPasswordRes
import com.poulastaa.data.model.payload.UpdatePasswordStatus
import com.poulastaa.domain.repository.ServiceRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.resetPassword(
    service: ServiceRepository,
) {
    route(EndPoints.SubmitNewPassword.route) {
        post {
            val req = call.receiveNullable<ResetPasswordReq>()
                ?: return@post call.respondRedirect(EndPoints.UnAuthorised.route)

            val status = service.updatePassword(
                token = req.token,
                password = req.password
            )

            val baseUrl = System.getenv("AUTH_URL")

            when (status) {
                UpdatePasswordStatus.RESET -> ResetPasswordRes(
                    status = status,
                    successUrl = "$baseUrl/.well-known/PasswordResetSuccess.html"
                )

                UpdatePasswordStatus.SAME_PASSWORD -> ResetPasswordRes(
                    status = status,
                    successUrl = ""
                )

                UpdatePasswordStatus.USER_NOT_FOUND -> ResetPasswordRes(
                    status = status,
                    successUrl = "$baseUrl/.well-known/PasswordResetNoUserFound.html"
                )

                UpdatePasswordStatus.NOT_PASSWORD -> ResetPasswordRes(
                    status = status,
                    successUrl = "$baseUrl/.well-known/PasswordResetNotValidPassword.html"
                )

                UpdatePasswordStatus.TOKEN_USED -> ResetPasswordRes(
                    status = status,
                    successUrl = "$baseUrl/.well-known/PasswordResetTokenUsed.html"
                )
            }.let {
                call.respond(
                    message = it,
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}
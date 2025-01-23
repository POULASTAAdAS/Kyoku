package com.poulastaa.auth.network.routes

import com.poulastaa.auth.domain.repository.AuthRepository
import com.poulastaa.auth.network.mapper.toUpdatePasswordStatus
import com.poulastaa.auth.network.model.NewPasswordRequest
import com.poulastaa.auth.network.model.ResetPasswordRes
import com.poulastaa.auth.network.model.UpdatePasswordStatus
import com.poulastaa.core.domain.model.EndPoints
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.changePassword(
    repo: AuthRepository,
) {
    route(EndPoints.SubmitNewPassword.route) {
        post {
            val req = call.receiveNullable<NewPasswordRequest>()
                ?: return@post call.respondRedirect(EndPoints.UnAuthorized.route)

            val status = repo.updatePassword(
                token = req.token,
                newPassword = req.password,
            ).toUpdatePasswordStatus()

            val baseUrl = System.getenv("BASE_URL")

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

                UpdatePasswordStatus.TOKEN_USED -> ResetPasswordRes(
                    status = status,
                    successUrl = "$baseUrl/.well-known/PasswordResetTokenUsed.html"
                )

                UpdatePasswordStatus.SERVER_ERROR -> ResetPasswordRes(
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
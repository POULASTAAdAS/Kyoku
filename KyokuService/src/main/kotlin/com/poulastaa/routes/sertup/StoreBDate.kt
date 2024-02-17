package com.poulastaa.routes.sertup

import com.poulastaa.data.model.EndPoints
import com.poulastaa.data.model.UserType
import com.poulastaa.data.model.setup.set_b_date.SetBDateReq
import com.poulastaa.data.model.setup.set_b_date.SetBDateResponse
import com.poulastaa.data.model.setup.set_b_date.SetBDateResponseStatus
import com.poulastaa.domain.repository.UserServiceRepository
import com.poulastaa.utils.Constants
import com.poulastaa.utils.getUserType
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.storeBDate(
    service: UserServiceRepository
) {
    authenticate(configurations = Constants.SECURITY_LIST) {
        route(EndPoints.StoreBDate.route) {
            post {
                val req = call.receiveNullable<SetBDateReq>() ?: return@post call.respond(
                    message = SetBDateResponse(
                        status = SetBDateResponseStatus.FAILURE
                    ),
                    status = HttpStatusCode.OK
                )

                val user = getUserType() ?: return@post call.respond(
                    message = SetBDateResponse(
                        status = SetBDateResponseStatus.FAILURE
                    ),
                    status = HttpStatusCode.OK
                )

                val response = when (user.userType) {
                    UserType.GOOGLE_USER -> {
                        service.storeBDate(req.date, user.userType, user.id)
                    }

                    UserType.EMAIL_USER -> {
                        service.storeBDate(req.date, user.userType, user.id)
                    }

                    UserType.PASSKEY_USER -> {
                        service.storeBDate(
                            date = req.date,
                            userType = user.userType,
                            id = req.email ?: return@post call.respond(
                                message = SetBDateResponse(
                                    status = SetBDateResponseStatus.FAILURE
                                ),
                                status = HttpStatusCode.OK
                            )
                        )
                    }
                }

                call.respond(
                    message = response,
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}
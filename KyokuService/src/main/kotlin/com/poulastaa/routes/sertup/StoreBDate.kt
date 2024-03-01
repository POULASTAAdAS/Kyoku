package com.poulastaa.routes.sertup

import com.poulastaa.data.model.common.EndPoints
import com.poulastaa.data.model.utils.UserType
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

                val helper = getUserType() ?: return@post call.respond(
                    message = SetBDateResponse(
                        status = SetBDateResponseStatus.FAILURE
                    ),
                    status = HttpStatusCode.OK
                )

                val response = when (helper.userType) {
                    UserType.GOOGLE_USER -> {
                        service.storeBDate(req.date, helper)
                    }

                    UserType.EMAIL_USER -> {
                        service.storeBDate(req.date, helper)
                    }

                    UserType.PASSKEY_USER -> {
                        service.storeBDate(
                            date = req.date,
                            helper = helper
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
package com.poulastaa.routes.auth

import com.poulastaa.data.model.EndPoints
import com.poulastaa.data.model.auth.req.AuthReqBaseModel
import com.poulastaa.data.model.auth.req.EmailLogInReq
import com.poulastaa.data.model.auth.req.EmailSignUpReq
import com.poulastaa.data.model.auth.req.GoogleAuthReq
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun Route.auth() {
    route(EndPoints.Auth.route) {
        post {
            val req = call.receiveText()

            val payload = try {
                Json.decodeFromString<AuthReqBaseModel>(req)
            } catch (e: Exception) {
                return@post call.respondRedirect(EndPoints.UnAuthorised.route)
            }

            when (payload) {
                is EmailLogInReq -> {

                }

                is EmailSignUpReq -> {

                }

                is GoogleAuthReq -> {

                }
            }
        }
    }
}
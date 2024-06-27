package com.poulastaa.domain.route_ext

import com.poulastaa.domain.model.GoogleUserSession
import com.poulastaa.domain.model.ReqUserPayload
import com.poulastaa.domain.model.UserType
import com.poulastaa.utils.Constants.ACCESS_TOKEN_CLAIM_KEY
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.sessions.*
import io.ktor.util.pipeline.*

fun PipelineContext<Unit, ApplicationCall>.getReqUserPayload(): ReqUserPayload? {
    val googleUser = call.sessions.get<GoogleUserSession>()

    // getting email from payload
    val emailUser = call.principal<JWTPrincipal>()?.payload
        ?.getClaim(ACCESS_TOKEN_CLAIM_KEY)?.asString()

    return if (googleUser != null) {
        ReqUserPayload(
            userType = UserType.GOOGLE_USER,
            id = googleUser.sub
        )
    } else if (emailUser != null) {
        ReqUserPayload(
            userType = UserType.EMAIL_USER,
            id = emailUser
        )
    } else {
        null
    }
}
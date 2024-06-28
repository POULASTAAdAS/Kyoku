package com.poulastaa.domain.route_ext

import com.poulastaa.domain.model.ReqUserPayload
import com.poulastaa.domain.model.UserSession
import com.poulastaa.domain.model.UserType
import com.poulastaa.utils.Constants.ACCESS_TOKEN_CLAIM_KEY
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.sessions.*

private fun ApplicationCall.updateCookie(
    userId: Long,
    email: String,
) {
    this.sessions.set(
        UserSession(
            userId = userId,
            email = email
        )
    )
}

fun ApplicationCall.getReqUserPayload(): ReqUserPayload? {
    val googleUser = this.sessions.get<UserSession>()

    // getting email from payload
    val emailUser = this.principal<JWTPrincipal>()?.payload
        ?.getClaim(ACCESS_TOKEN_CLAIM_KEY)?.asString()

    return if (googleUser != null) {
        updateCookie(
            userId = googleUser.userId,
            email = googleUser.email
        )

        ReqUserPayload(
            userType = UserType.GOOGLE_USER,
            id = googleUser.userId.toString()
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
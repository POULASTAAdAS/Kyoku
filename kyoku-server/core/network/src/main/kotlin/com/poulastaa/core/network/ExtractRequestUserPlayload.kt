package com.poulastaa.core.network

import com.poulastaa.core.domain.model.ReqUserPayload
import com.poulastaa.core.domain.model.UserType
import com.poulastaa.core.domain.repository.auth.JWTRepository
import com.poulastaa.core.network.model.UserSession
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.sessions.*

private fun ApplicationCall.updateCookie(
    userId: Long,
    email: String,
) {
    this.sessions.set<UserSession>(
        UserSession(
            userId = userId,
            email = email
        )
    )
}

fun ApplicationCall.getReqUserPayload(): ReqUserPayload? {
    val session = this.sessions.get<UserSession>()

    val email = this.principal<JWTPrincipal>()?.payload
        ?.getClaim(JWTRepository.TokenType.TOKEN_ACCESS.claimKey)?.asString()

    return if (email != null) ReqUserPayload(
        email = email,
        userType = UserType.EMAIL
    ) else if (session != null) {
        updateCookie(
            userId = session.userId,
            email = session.email
        )

        ReqUserPayload(
            id = session.userId,
            email = session.email,
            userType = UserType.GOOGLE
        )
    } else null
}
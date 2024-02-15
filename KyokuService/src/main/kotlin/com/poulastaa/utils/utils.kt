package com.poulastaa.utils

import com.poulastaa.data.model.FindUserType
import com.poulastaa.data.model.UserType
import com.poulastaa.data.model.auth.GoogleUserSession
import com.poulastaa.data.model.auth.PasskeyUserSession
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.sessions.*
import io.ktor.util.pipeline.*

fun PipelineContext<Unit, ApplicationCall>.getUserType(): FindUserType? {
    val googleUser = call.sessions.get<GoogleUserSession>()
    val passkeyUser = call.sessions.get<PasskeyUserSession>()

    val emailUser = call.principal<JWTPrincipal>()?.payload
        ?.getClaim(Constants.ACCESS_TOKEN_CLAIM_KEY)?.asString()


    return if (googleUser != null) {
        FindUserType(
            userType = UserType.GOOGLE_USER,
            id = googleUser.sub
        )
    } else if (passkeyUser != null) {
        FindUserType(
            userType = UserType.PASSKEY_USER,
            id = passkeyUser.id
        )
    } else if (emailUser != null) {
        FindUserType(
            userType = UserType.EMAIL_USER,
            id = emailUser
        )
    } else {
        return null
    }
}
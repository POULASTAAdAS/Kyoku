package com.poulastaa.utils

import com.poulastaa.data.model.auth.GoogleUserSession
import com.poulastaa.data.model.auth.PasskeyUserSession
import com.poulastaa.data.model.utils.UserType
import com.poulastaa.data.model.utils.UserTypeHelper
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.sessions.*
import io.ktor.util.pipeline.*

fun PipelineContext<Unit, ApplicationCall>.getUserType(): UserTypeHelper? {
    val googleUser = call.sessions.get<GoogleUserSession>()
    val passkeyUser = call.sessions.get<PasskeyUserSession>()

    val emailUser = call.principal<JWTPrincipal>()?.payload
        ?.getClaim(Constants.ACCESS_TOKEN_CLAIM_KEY)?.asString()


    return if (googleUser != null) {
        UserTypeHelper(
            userType = UserType.GOOGLE_USER,
            headerId = googleUser.sub
        )
    } else if (passkeyUser != null) {
        UserTypeHelper(
            userType = UserType.PASSKEY_USER,
            headerId = passkeyUser.id
        )
    } else if (emailUser != null) {
        UserTypeHelper(
            userType = UserType.EMAIL_USER,
            headerId = emailUser
        )
    } else {
        return null
    }
}
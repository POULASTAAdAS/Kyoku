package com.example.util

import com.example.util.Constants.ACCESS_TOKEN_CLAIM_KEY
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun ApplicationCall.getClaimFromPayload(): String? =
    this.authentication.principal<JWTPrincipal>()?.payload?.getClaim(ACCESS_TOKEN_CLAIM_KEY)?.asString()

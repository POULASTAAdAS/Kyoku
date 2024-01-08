package com.example.util

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun ApplicationCall.getClaimFromPayload(): String? =
    this.authentication.principal<JWTPrincipal>()?.payload?.getClaim("email")?.asString()

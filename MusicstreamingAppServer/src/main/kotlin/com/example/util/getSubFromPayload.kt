package com.example.util

import com.example.data.model.GoogleUserSession
import io.ktor.server.application.*
import io.ktor.server.auth.*

fun ApplicationCall.getSub(): String? =
    this.authentication.principal<GoogleUserSession>()?.sub
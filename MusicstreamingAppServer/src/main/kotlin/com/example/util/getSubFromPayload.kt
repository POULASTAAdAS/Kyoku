package com.example.util

import com.example.data.model.GoogleUserSession
import io.ktor.server.application.*
import io.ktor.server.auth.*

fun ApplicationCall.getEmail(): String? =
    this.authentication.principal<GoogleUserSession>()?.email
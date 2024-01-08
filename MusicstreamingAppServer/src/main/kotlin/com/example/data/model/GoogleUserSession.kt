package com.example.data.model

import io.ktor.server.auth.*

data class GoogleUserSession(
    val sub: String,
    val name: String
) : Principal

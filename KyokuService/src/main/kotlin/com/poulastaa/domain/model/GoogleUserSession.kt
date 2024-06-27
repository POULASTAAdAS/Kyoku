package com.poulastaa.domain.model

import io.ktor.server.auth.*

data class GoogleUserSession(
    val sub: String,
    val userName: String,
) : Principal
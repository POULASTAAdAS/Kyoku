package com.poulastaa.data.model.auth.google

import io.ktor.server.auth.*

data class GoogleUserSession(
    val sub: String,
    val userName: String
) : Principal

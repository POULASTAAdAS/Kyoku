package com.poulastaa.data.model.auth

import io.ktor.server.auth.*

data class GoogleUserSession(
    val email: String,
    val userName: String
) : Principal

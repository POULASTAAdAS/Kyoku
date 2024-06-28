package com.poulastaa.domain.model

import io.ktor.server.auth.*

data class UserSession(
     val userId: Long,
    val email: String,
) : Principal
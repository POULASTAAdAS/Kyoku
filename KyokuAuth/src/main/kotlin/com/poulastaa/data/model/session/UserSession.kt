package com.poulastaa.data.model.session

import io.ktor.server.auth.*

data class UserSession(
    val userId: Long,
    val email: String,
) : Principal

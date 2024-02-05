package com.poulastaa.data.model.auth

import io.ktor.server.auth.*
import kotlinx.serialization.Serializable

@Serializable
data class PasskeyUserSession(
    val id: String,
    val userName: String
): Principal

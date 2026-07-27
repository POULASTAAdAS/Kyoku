package com.poulastaa.auth.domain

interface GoogleTokenExchange {
    suspend fun exchange(
        clientId: String,
        redirectUri: String,
        code: String,
        verifier: String,
    ): String
}

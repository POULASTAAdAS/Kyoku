package com.poulastaa.auth.domain

interface GoogleTokenExchange {
    suspend fun exchange(
        clientId: String,
        clientSecret: String,
        redirectUri: String,
        code: String,
        verifier: String,
    ): String
}

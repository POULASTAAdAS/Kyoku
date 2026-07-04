package com.poulastaa.auth.domain

interface AuthRemoteDatasource {
    suspend fun signIn(email: String, password: String): Unit
}
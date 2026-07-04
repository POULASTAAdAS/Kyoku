package com.poulastaa.auth.domain

interface AuthRepository {
    suspend fun signIn(email: String, password: String): Unit
}
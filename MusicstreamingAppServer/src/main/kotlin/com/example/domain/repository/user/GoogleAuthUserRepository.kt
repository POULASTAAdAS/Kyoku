package com.example.domain.repository.user

import com.example.data.model.auth.UserCreationResponse

interface GoogleAuthUserRepository {
    suspend fun createUser(
        userName: String,
        email: String,
        sub: String,
        pictureUrl: String
    ): UserCreationResponse
}
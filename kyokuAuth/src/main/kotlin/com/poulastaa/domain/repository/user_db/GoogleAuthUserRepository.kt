package com.poulastaa.domain.repository.user_db

import com.poulastaa.data.model.auth.res.GoogleSignInResponse


interface GoogleAuthUserRepository {
    suspend fun createUser(
        userName: String,
        email: String,
        sub: String,
        pictureUrl: String
    ): GoogleSignInResponse
}
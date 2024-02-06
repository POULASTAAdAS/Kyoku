package com.poulastaa.domain.repository.user_db

import com.poulastaa.data.model.auth.passkey.PasskeyAuthResponse
import com.poulastaa.domain.dao.PasskeyAuthUser

interface PasskeyAuthUserRepository {
    suspend fun findUserByEmail(email: String): PasskeyAuthUser?

    suspend fun createUser(
        userId: String,
        email: String,
        userName: String,
        profilePic:String
    ): PasskeyAuthResponse

    suspend fun getUser(
        userId: String
    ): PasskeyAuthUser?
}
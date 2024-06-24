package com.poulastaa.domain.repository

import com.poulastaa.data.model.GoogleAuthResPayload
import com.poulastaa.data.model.auth.res.Payload
import com.poulastaa.data.model.auth.response.EmailAuthRes
import com.poulastaa.data.model.auth.response.UserAuthStatus

interface AuthRepository {
    suspend fun createEmailUser(
        userName: String,
        email: String,
        password: String,
        refreshToken: String,
        countryId: Int,
    ): UserAuthStatus

    suspend fun loginEmailUser(
        email: String,
        password: String,
        refreshToken: String,
    ): EmailAuthRes

    suspend fun googleAuth(
        payload: Payload,
        countryId: Int,
    ): GoogleAuthResPayload
}
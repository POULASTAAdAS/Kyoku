package com.poulastaa.domain.repository

import com.poulastaa.data.model.auth.response.Payload
import com.poulastaa.data.model.auth.response.UserAuthRes
import com.poulastaa.data.model.auth.response.UserAuthStatus
import com.poulastaa.data.model.payload.GoogleAuthResPayload
import com.poulastaa.data.model.payload.UpdateEmailVerificationPayload
import com.poulastaa.data.model.payload.UpdatePasswordStatus

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
    ): UserAuthRes

    suspend fun googleAuth(
        payload: Payload,
        countryId: Int,
    ): GoogleAuthResPayload

    suspend fun updateSignUpEmailVerificationStatus(
        email: String,
    ): UpdateEmailVerificationPayload

    suspend fun updateLogInEmailVerificationStatus(
        email: String,
    ): UpdateEmailVerificationPayload

    suspend fun signUpEmailVerificationCheck(
        email: String,
    ): Boolean

    suspend fun logInEmailVerificationCheck(
        email: String,
    ): Boolean

    suspend fun updatePassword(
        password: String,
        email: String,
    ): UpdatePasswordStatus
}
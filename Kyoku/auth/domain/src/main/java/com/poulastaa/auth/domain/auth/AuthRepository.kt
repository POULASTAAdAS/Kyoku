package com.poulastaa.auth.domain.auth

import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result


interface AuthRepository {
    suspend fun emailSingUp(
        email: String,
        password: String,
        username: String,
        countryCode: String,
    ): Result<UserAuthStatus, DataError.Network>

    suspend fun emailLogIn(
        email: String,
        password: String,
    ): Result<UserAuthStatus, DataError.Network>

    suspend fun googleAuth(
        token: String,
        countryCode: String,
    ): Result<UserAuthStatus, DataError.Network>

    suspend fun emailSignupStatusCheck(
        email: String,
    ): Boolean

    suspend fun emailLoginStatusCheck(
        email: String,
    ): Boolean
}
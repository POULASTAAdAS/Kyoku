package com.poulastaa.core.domain.repository.auth

import com.poulastaa.core.domain.model.ForgotPasswordSetStatus
import com.poulastaa.core.domain.model.UserAuthStatus
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

    suspend fun sendForgotPasswordMail(
        email: String,
    ): Result<ForgotPasswordSetStatus, DataError.Network>
}
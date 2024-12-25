package com.poulastaa.auth.domain

import com.poulastaa.auth.domain.model.AuthStatus
import com.poulastaa.auth.domain.model.ForgotPasswordStatus
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result

interface AuthRepository {
    suspend fun emailSignUp(
        email: String,
        password: String,
        username: String,
        countryCode: String,
    ): Result<AuthStatus, DataError.Network>

    suspend fun emailLogIn(
        email: String,
        password: String,
    ): Result<AuthStatus, DataError.Network>

    suspend fun googleAuth(
        token: String,
        countryCode: String,
    ): Result<AuthStatus, DataError.Network>

    suspend fun checkEmailVerificationState(
        email: String,
        resultState: AuthStatus,
    ): Result<Boolean, DataError.Network>

    suspend fun sendForgotPasswordMail(email: String): Result<ForgotPasswordStatus, DataError.Network>
}
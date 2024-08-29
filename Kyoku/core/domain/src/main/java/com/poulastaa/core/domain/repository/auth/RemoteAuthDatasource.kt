package com.poulastaa.core.domain.repository.auth

import com.poulastaa.core.domain.model.AuthData
import com.poulastaa.core.domain.model.EmailVerification
import com.poulastaa.core.domain.model.ForgotPasswordSetStatus
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result

interface RemoteAuthDatasource {
    suspend fun emailSingUp(
        email: String,
        password: String,
        username: String,
        countryCode: String,
    ): Result<AuthData, DataError.Network>

    suspend fun emailLogIn(
        email: String,
        password: String,
    ): Result<AuthData, DataError.Network>

    suspend fun googleAuth(
        token: String,
        countryCode: String,
    ): Result<AuthData, DataError.Network>

    suspend fun emailSignupStatusCheck(
        email: String,
    ): Result<EmailVerification, DataError.Network>

    suspend fun emailLoginStatusCheck(
        email: String,
    ): Result<EmailVerification, DataError.Network>

    suspend fun sendForgotPasswordMail(
        email: String,
    ): Result<ForgotPasswordSetStatus, DataError.Network>
}
package com.poulastaa.auth.domain

import com.poulastaa.auth.domain.model.AuthResponseDto
import com.poulastaa.auth.domain.model.EmailVerificationStatus
import com.poulastaa.auth.domain.model.ForgotPasswordStatus
import com.poulastaa.auth.domain.model.JwtTokenDto
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result

interface RemoteAuthDataSource {
    suspend fun emailSingUp(
        email: String,
        password: String,
        username: String,
        countryCode: String,
    ): Result<AuthResponseDto, DataError.Network>

    suspend fun emailLogIn(
        email: String,
        password: String,
    ): Result<AuthResponseDto, DataError.Network>

    suspend fun googleAuth(
        token: String,
        countryCode: String,
    ): Result<AuthResponseDto, DataError.Network>

    suspend fun checkEmailVerificationState(
        email: String,
    ): Result<EmailVerificationStatus, DataError.Network>

    suspend fun getJWTToken(email: String): Result<JwtTokenDto, DataError.Network>
    suspend fun sendForgotPasswordMail(email: String): Result<ForgotPasswordStatus, DataError.Network>
}
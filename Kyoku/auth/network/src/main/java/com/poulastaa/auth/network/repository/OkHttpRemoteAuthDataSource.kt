package com.poulastaa.auth.network.repository

import com.google.gson.Gson
import com.poulastaa.auth.domain.RemoteAuthDataSource
import com.poulastaa.auth.domain.model.AuthResponseDto
import com.poulastaa.auth.domain.model.EmailVerificationStatus
import com.poulastaa.auth.domain.model.ForgotPasswordStatus
import com.poulastaa.auth.domain.model.JwtTokenDto
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import okhttp3.OkHttpClient
import javax.inject.Inject

class OkHttpRemoteAuthDataSource @Inject constructor(
    private val gson: Gson,
    private val client: OkHttpClient,
) : RemoteAuthDataSource {
    override suspend fun emailSingUp(
        email: String,
        password: String,
        username: String,
        countryCode: String,
    ): Result<AuthResponseDto, DataError.Network> {
        TODO("Not yet implemented")
    }

    override suspend fun emailLogIn(
        email: String,
        password: String,
    ): Result<AuthResponseDto, DataError.Network> {
        TODO("Not yet implemented")
    }

    override suspend fun googleAuth(
        token: String,
        countryCode: String,
    ): Result<AuthResponseDto, DataError.Network> {
        TODO("Not yet implemented")
    }

    override suspend fun checkEmailVerificationState(email: String): Result<EmailVerificationStatus, DataError.Network> {
        TODO("Not yet implemented")
    }

    override suspend fun getJWTToken(email: String): Result<JwtTokenDto, DataError.Network> {
        TODO("Not yet implemented")
    }

    override suspend fun sendForgotPasswordMail(email: String): Result<ForgotPasswordStatus, DataError.Network> {
        TODO("Not yet implemented")
    }
}
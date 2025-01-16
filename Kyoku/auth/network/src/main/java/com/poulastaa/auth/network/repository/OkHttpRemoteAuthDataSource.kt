package com.poulastaa.auth.network.repository

import com.google.gson.Gson
import com.poulastaa.auth.domain.RemoteAuthDataSource
import com.poulastaa.auth.domain.model.AuthResponseDto
import com.poulastaa.auth.domain.model.ForgotPasswordStatus
import com.poulastaa.auth.domain.model.JwtTokenDto
import com.poulastaa.auth.network.model.AuthenticationResponse
import com.poulastaa.auth.network.model.EmailLogInRequest
import com.poulastaa.auth.network.model.EmailSignUpRequest
import com.poulastaa.auth.network.model.ForgotPasswordResponse
import com.poulastaa.auth.network.model.GoogleAuthRequest
import com.poulastaa.auth.network.model.JwtTokenResponse
import com.poulastaa.auth.network.toAuthResponseDto
import com.poulastaa.auth.network.toForgotPasswordStatus
import com.poulastaa.auth.network.toJWTTokenDto
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.map
import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.network.req
import okhttp3.OkHttpClient
import javax.inject.Inject
import javax.inject.Named

class OkHttpRemoteAuthDataSource @Inject constructor(
    private val gson: Gson,
    @Named("AuthClient") private val client: OkHttpClient,
) : RemoteAuthDataSource {
    override suspend fun emailSingUp(
        email: String,
        password: String,
        username: String,
        countryCode: String,
    ): Result<AuthResponseDto, DataError.Network> {
        val result = client.req<EmailSignUpRequest, AuthenticationResponse>(
            route = EndPoints.Auth.route,
            method = com.poulastaa.core.network.ApiMethodType.POST,
            gson = gson,
            body = EmailSignUpRequest(
                email = email,
                password = password,
                username = username,
                countryCode = countryCode
            )
        )

        return result.map { it.toAuthResponseDto() }
    }

    override suspend fun emailLogIn(
        email: String,
        password: String,
    ): Result<AuthResponseDto, DataError.Network> {
        val result = client.req<EmailLogInRequest, AuthenticationResponse>(
            route = EndPoints.Auth.route,
            method = com.poulastaa.core.network.ApiMethodType.POST,
            gson = gson,
            body = EmailLogInRequest(
                email = email,
                password = password,
            )
        )

        return result.map { it.toAuthResponseDto() }
    }

    override suspend fun googleAuth(
        token: String,
        countryCode: String,
    ): Result<AuthResponseDto, DataError.Network> {
        val result = client.req<GoogleAuthRequest, AuthenticationResponse>(
            route = EndPoints.Auth.route,
            method = com.poulastaa.core.network.ApiMethodType.POST,
            gson = gson,
            body = GoogleAuthRequest(
                token = token,
                countryCode = countryCode
            )
        )

        return result.map { it.toAuthResponseDto() }
    }

    override suspend fun checkEmailVerificationState(email: String): Result<JwtTokenDto, DataError.Network> {
        val result = client.req<Unit, JwtTokenResponse>(
            route = EndPoints.GetJWTToken.route,
            method = com.poulastaa.core.network.ApiMethodType.GET,
            gson = gson,
            params = listOf(
                com.poulastaa.core.network.ReqParam(
                    key = "email",
                    value = email
                )
            )
        )

        return result.map { it.toJWTTokenDto() }
    }

    override suspend fun sendForgotPasswordMail(email: String): Result<ForgotPasswordStatus, DataError.Network> {
        val result = client.req<Unit, ForgotPasswordResponse>(
            route = EndPoints.ForgotPassword.route,
            method = com.poulastaa.core.network.ApiMethodType.GET,
            gson = gson,
            params = listOf(
                com.poulastaa.core.network.ReqParam(
                    key = "email",
                    value = email
                )
            )
        )

        return result.map { it.status.toForgotPasswordStatus() }
    }
}
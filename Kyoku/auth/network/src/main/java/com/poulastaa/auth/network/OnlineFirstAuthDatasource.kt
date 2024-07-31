package com.poulastaa.auth.network

import com.google.gson.Gson
import com.poulastaa.auth.network.req.EmailLogInReq
import com.poulastaa.auth.network.req.EmailSignUpReq
import com.poulastaa.auth.network.req.GoogleAuthReq
import com.poulastaa.auth.network.res.AuthDto
import com.poulastaa.auth.network.res.EmailVerificationDto
import com.poulastaa.auth.network.res.ForgotPasswordSetStatusDto
import com.poulastaa.core.data.network.authGet
import com.poulastaa.core.data.network.post
import com.poulastaa.core.domain.EndPoints
import com.poulastaa.core.domain.auth.RemoteAuthDatasource
import com.poulastaa.core.domain.model.AuthData
import com.poulastaa.core.domain.model.EmailVerification
import com.poulastaa.core.domain.model.ForgotPasswordSetStatus
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.domain.utils.map
import okhttp3.OkHttpClient
import javax.inject.Inject
import javax.inject.Named

class OnlineFirstAuthDatasource @Inject constructor(
    @Named("AuthClient") private val client: OkHttpClient,
    private val gson: Gson,
) : RemoteAuthDatasource {
    override suspend fun emailSingUp(
        email: String,
        password: String,
        username: String,
        countryCode: String,
    ): Result<AuthData, DataError.Network> {
        val response = client.post<EmailSignUpReq, AuthDto>(
            route = EndPoints.Auth.route,
            body = EmailSignUpReq(
                email = email,
                password = password,
                userName = username,
                countryCode = countryCode
            ),
            gson = gson,
            auth = true
        )

        return response.map { it.toAuthData() }
    }

    override suspend fun emailLogIn(
        email: String,
        password: String,
    ): Result<AuthData, DataError.Network> {
        val response = client.post<EmailLogInReq, AuthDto>(
            route = EndPoints.Auth.route,
            body = EmailLogInReq(
                email = email,
                password = password,
            ),
            gson = gson,
            auth = true
        )

        return response.map { it.toAuthData() }
    }

    override suspend fun googleAuth(
        token: String,
        countryCode: String,
    ): Result<AuthData, DataError.Network> {
        val response = client.post<GoogleAuthReq, AuthDto>(
            route = EndPoints.Auth.route,
            body = GoogleAuthReq(
                token = token,
                countryCode = countryCode
            ),
            gson = gson,
            auth = true
        )

        return response.map { it.toAuthData() }
    }

    override suspend fun emailSignupStatusCheck(email: String): Result<EmailVerification, DataError.Network> {
        val response = client.authGet<EmailVerificationDto>(
            route = EndPoints.SignUpEmailVerificationCheck.route,
            params = listOf(
                Pair(
                    first = "email",
                    second = email
                )
            ),
            gson = gson
        )

        return response.map { it.toEmailVerification() }
    }

    override suspend fun emailLoginStatusCheck(email: String): Result<EmailVerification, DataError.Network> {
        val response = client.authGet<EmailVerificationDto>(
            route = EndPoints.LogInEmailVerificationCheck.route,
            params = listOf(
                Pair(
                    first = "email",
                    second = email
                )
            ),
            gson = gson
        )

        return response.map { it.toEmailVerification() }
    }

    override suspend fun sendForgotPasswordMail(email: String): Result<ForgotPasswordSetStatus, DataError.Network> {
        val response = client.authGet<ForgotPasswordSetStatusDto>(
            route = EndPoints.ForgotPassword.route,
            params = listOf(
                Pair(
                    first = "email",
                    second = email
                )
            ),
            gson = gson
        )

        return response.map {
            when (it) {
                ForgotPasswordSetStatusDto.SENT -> ForgotPasswordSetStatus.SENT
                ForgotPasswordSetStatusDto.NO_USER_FOUND -> ForgotPasswordSetStatus.NO_USER_FOUND
            }
        }
    }
}
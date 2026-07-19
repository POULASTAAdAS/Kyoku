package com.poulastaa.auth.network

import com.poulastaa.auth.domain.AuthRemoteDatasource
import com.poulastaa.auth.domain.model.DtoAuthResponse
import com.poulastaa.auth.domain.model.DtoEmailAuthResponse
import com.poulastaa.auth.domain.model.DtoForgotPasswordResponse
import com.poulastaa.auth.network.model.AuthResponse
import com.poulastaa.auth.network.model.ForgotPasswordResponse
import com.poulastaa.auth.network.model.GoogleAuthRequest
import com.poulastaa.auth.network.model.GoogleAuthResponse
import com.poulastaa.auth.network.model.SignInRequest
import com.poulastaa.auth.network.model.SignUpRequest
import com.poulastaa.common.domain.model.DtoTokens
import com.poulastaa.common.network.ApiEndpoints
import com.poulastaa.common.network.ApiError
import com.poulastaa.common.network.ApiRequestType
import com.poulastaa.common.network.ApiResult
import com.poulastaa.common.network.map
import com.poulastaa.common.network.model.ResponseTokens
import com.poulastaa.common.network.req
import com.poulastaa.platfrom.PlatformUtils
import io.ktor.client.HttpClient
import org.koin.core.annotation.Single

@Single(binds = [AuthRemoteDatasource::class])
class KtorAuthDatasource(
    private val client: HttpClient,
) : AuthRemoteDatasource {
    override suspend fun signIn(
        email: String,
        password: String,
    ): ApiResult<DtoEmailAuthResponse, ApiError> =
        client.req<SignInRequest, AuthResponse, ApiError.Authentication>(
            route = ApiEndpoints.Auth.SIGN_IN,
            type = ApiRequestType.POST,
            body = SignInRequest(email, password),
        ).map(AuthResponse::toDto)

    override suspend fun checkVerificationStatus(email: String): ApiResult<DtoTokens, ApiError> =
        client.req<Unit, ResponseTokens, ApiError.Authentication>(
            route = ApiEndpoints.Auth.CHECK_VERIFICATION_MAIL_STATE,
            type = ApiRequestType.GET,
            params = listOf(
                "email" to email,
                "type" to EMAIL_USER_TYPE,
            ),
        ).map(ResponseTokens::toDto)

    override suspend fun signUp(
        email: String,
        username: String,
        password: String,
    ): ApiResult<DtoEmailAuthResponse, ApiError> =
        client.req<SignUpRequest, AuthResponse, ApiError.Authentication>(
            route = ApiEndpoints.Auth.SIGN_UP,
            type = ApiRequestType.POST,
            body = SignUpRequest(
                email = email,
                username = username,
                password = password,
                countryCode = PlatformUtils.countryCode,
            ),
        ).map(AuthResponse::toDto)

    override suspend fun googleAuth(
        token: String,
        countryCode: String,
    ): ApiResult<DtoAuthResponse, ApiError> =
        client.req<GoogleAuthRequest, GoogleAuthResponse, ApiError.Authentication>(
            route = ApiEndpoints.Auth.GOOGLE_AUTH,
            type = ApiRequestType.POST,
            body = GoogleAuthRequest(
                token = token,
                code = countryCode,
            ),
        ).map(GoogleAuthResponse::toDto)

    override suspend fun sendForgotPasswordMail(email: String): ApiResult<DtoForgotPasswordResponse, ApiError> =
        client.req<Unit, ForgotPasswordResponse, ApiError.Authentication>(
            route = ApiEndpoints.Auth.FORGOT_PASSWORD,
            type = ApiRequestType.GET,
            params = listOf("email" to email),
        ).map(ForgotPasswordResponse::toDto)

    private companion object {
        const val EMAIL_USER_TYPE = "EMAIL"
    }
}

package com.poulastaa.auth.network

import com.poulastaa.auth.domain.AuthRemoteDatasource
import com.poulastaa.auth.domain.model.DtoAuthResponse
import com.poulastaa.auth.domain.model.DtoForgotPasswordResponse
import com.poulastaa.auth.network.model.AuthResponse
import com.poulastaa.auth.network.model.ForgotPasswordResponse
import com.poulastaa.auth.network.model.GoogleAuthRequest
import com.poulastaa.auth.network.model.SignInRequest
import com.poulastaa.common.network.ApiEndpoints
import com.poulastaa.common.network.ApiError
import com.poulastaa.common.network.ApiRequestType
import com.poulastaa.common.network.ApiResult
import com.poulastaa.common.network.ErrorResponse
import com.poulastaa.common.network.map
import com.poulastaa.common.network.req
import io.ktor.client.HttpClient
import org.koin.core.annotation.Single

@Single(binds = [AuthRemoteDatasource::class])
class KtorAuthDatasource(
    private val client: HttpClient,
) : AuthRemoteDatasource {
    override suspend fun signIn(
        email: String,
        password: String,
    ): ApiResult<DtoAuthResponse, ApiError> =
        client.req<SignInRequest, AuthResponse, ApiError.Authentication>(
            route = ApiEndpoints.Auth.SIGN_IN,
            type = ApiRequestType.POST,
            body = SignInRequest(email, password),
        ).map { it.toDto() }

    override suspend fun googleAuth(
        token: String,
        countryCode: String,
    ): ApiResult<DtoAuthResponse, ApiError> =
        client.req<GoogleAuthRequest, AuthResponse, ApiError.Authentication>(
            route = ApiEndpoints.Auth.GOOGLE_AUTH,
            type = ApiRequestType.POST,
            body = GoogleAuthRequest(
                token = token,
                code = countryCode,
            ),
        ).map { it.toDto() }

    override suspend fun sendForgotPasswordMail(email: String): ApiResult<DtoForgotPasswordResponse, ApiError> =
        client.req<Unit, ForgotPasswordResponse, ApiError.Authentication>(
            route = ApiEndpoints.Auth.FORGOT_PASSWORD,
            type = ApiRequestType.GET,
            params = listOf("email" to email),
        ).toForgotPasswordApiResult()

    private fun ApiResult<ForgotPasswordResponse, ApiError>.toForgotPasswordApiResult(): ApiResult<DtoForgotPasswordResponse, ApiError> =
        when (this) {
            is ApiResult.Error -> this
            is ApiResult.Success -> when (response) {
                ForgotPasswordResponse.SENT -> ApiResult.Success(response.toDto())
                ForgotPasswordResponse.USER_NOT_FOUND -> ApiResult.Error(
                    error = ErrorResponse(
                        error = ApiError.Authentication.ACCOUNT_NOT_FOUND,
                        message = ApiError.Authentication.ACCOUNT_NOT_FOUND.message,
                    )
                )

                ForgotPasswordResponse.INVALID_EMAIL -> ApiResult.Error(
                    error = ErrorResponse(
                        error = ApiError.Authentication.INVALID_EMAIL,
                        message = ApiError.Authentication.INVALID_EMAIL.message,
                    )
                )

                ForgotPasswordResponse.ERROR -> ApiResult.Error(
                    error = ErrorResponse(
                        error = ApiError.Network.SOMETHING_WENT_WRONG,
                        message = ApiError.Network.SOMETHING_WENT_WRONG.message,
                    )
                )
            }
        }
}

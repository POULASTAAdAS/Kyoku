package com.poulastaa.auth.network

import com.poulastaa.auth.domain.AuthRemoteDatasource
import com.poulastaa.auth.domain.model.DtoAuthResponse
import com.poulastaa.auth.network.model.AuthResponse
import com.poulastaa.auth.network.model.SignInRequest
import com.poulastaa.common.network.ApiEndpoints
import com.poulastaa.common.network.ApiError
import com.poulastaa.common.network.ApiRequestType
import com.poulastaa.common.network.ApiResult
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
    ): ApiResult<DtoAuthResponse, ApiError> = client.req<SignInRequest, AuthResponse, ApiError.Authentication>(
        route = ApiEndpoints.Auth.SIGN_IN,
        type = ApiRequestType.POST,
        body = SignInRequest(email, password),
    ).map { it.toDto() }
}

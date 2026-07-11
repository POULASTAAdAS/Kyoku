package com.poulastaa.auth.network

import com.poulastaa.auth.domain.AuthRemoteDatasource
import com.poulastaa.auth.network.model.SignInRequest
import com.poulastaa.common.network.ApiError
import com.poulastaa.common.network.ApiRequestType
import com.poulastaa.common.network.ApiResult
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
    ): ApiResult<Unit, ApiError> {
        val a = client.req<SignInRequest, Unit>(
            route = "/auth/sign-in",
            type = ApiRequestType.POST,
            body = SignInRequest(email, password),
        )

        TODO()
    }
}

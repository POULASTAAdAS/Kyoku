package com.poulastaa.auth.network

import com.poulastaa.auth.domain.AuthRemoteDatasource
import com.poulastaa.common.network.ApiError
import com.poulastaa.common.network.ApiResult
import io.ktor.client.HttpClient
import org.koin.core.annotation.Single

@Single(binds = [AuthRemoteDatasource::class])
class KtorAuthDatasource(
    private val client: HttpClient,
) : AuthRemoteDatasource {
    override suspend fun signIn(
        email: String,
        password: String,
    ): ApiResult<Unit, ApiError.Authentication> {
        return ApiResult.Success(Unit)
    }
}

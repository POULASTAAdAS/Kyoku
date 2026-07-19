package com.poulastaa.auth.data

import com.poulastaa.auth.domain.AuthLocalDatasource
import com.poulastaa.auth.domain.AuthRemoteDatasource
import com.poulastaa.auth.domain.AuthRepository
import com.poulastaa.auth.domain.model.DtoAuthResponse
import com.poulastaa.auth.domain.model.DtoForgotPasswordResponse
import com.poulastaa.common.domain.model.DtoUser
import com.poulastaa.common.network.ApiError
import com.poulastaa.common.network.ApiResult
import com.poulastaa.common.network.map
import org.koin.core.annotation.Single

@Single(binds = [AuthRepository::class])
class RemoteAuthRepository(
    private val remote: AuthRemoteDatasource,
    private val local: AuthLocalDatasource,
) : AuthRepository {
    override suspend fun signIn(
        email: String,
        password: String,
    ): ApiResult<DtoUser, ApiError> {
        val response = remote.signIn(email, password)
        response.saveAuthData()

        return response.map { it.user }
    }

    override suspend fun signUp(
        email: String,
        username: String,
        password: String,
    ): ApiResult<DtoUser, ApiError> {
        val response = remote.signUp(email, username, password)
        response.saveAuthData()

        return response.map { it.user }
    }

    override suspend fun googleAuth(
        token: String,
        countryCode: String,
    ): ApiResult<DtoUser, ApiError> {
        val response = remote.googleAuth(token, countryCode)
        response.saveAuthData()

        return response.map { it.user }
    }

    override suspend fun sendForgotPasswordMail(email: String): ApiResult<DtoForgotPasswordResponse, ApiError> =
        remote.sendForgotPasswordMail(email)

    private suspend fun ApiResult<DtoAuthResponse, ApiError>.saveAuthData() {
        if (this is ApiResult.Success) {
            local.saveUser(response.user)
            local.saveTokens(response.tokens)
        }
    }
}

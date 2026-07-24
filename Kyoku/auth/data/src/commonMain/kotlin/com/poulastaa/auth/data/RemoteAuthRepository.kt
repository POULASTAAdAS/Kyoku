package com.poulastaa.auth.data

import com.poulastaa.auth.domain.AuthLocalDatasource
import com.poulastaa.auth.domain.AuthRemoteDatasource
import com.poulastaa.auth.domain.AuthRepository
import com.poulastaa.auth.domain.model.DtoAuthResponse
import com.poulastaa.auth.domain.model.DtoForgotPasswordResponse
import com.poulastaa.common.domain.model.DtoUser
import com.poulastaa.common.network.ApiError
import com.poulastaa.common.network.AppResult
import com.poulastaa.common.network.asEmptyResponse
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
    ): AppResult<Boolean, ApiError> {
        val result = remote.signIn(email, password)
        if (result is AppResult.Success) local.saveUser(result.response.user)
        return result.map { it.user.isNewUser }
    }

    override suspend fun signUp(
        email: String,
        username: String,
        password: String,
    ): AppResult<Boolean, ApiError> {
        val result = remote.signUp(email, username, password)
        if (result is AppResult.Success) local.saveUser(result.response.user)
        return result.map { true }
    }

    override suspend fun checkVerificationStatus(email: String): AppResult<Unit, ApiError> {
        val result = remote.checkVerificationStatus(email)
        if (result is AppResult.Success) local.saveTokens(result.response)

        return result.asEmptyResponse()
    }


    override suspend fun googleAuth(
        token: String,
        countryCode: String,
    ): AppResult<DtoUser, ApiError> {
        val response = remote.googleAuth(token, countryCode)
        response.saveAuthData()

        return response.map { it.user }
    }

    override suspend fun sendForgotPasswordMail(email: String): AppResult<DtoForgotPasswordResponse, ApiError> =
        remote.sendForgotPasswordMail(email)

    private suspend fun AppResult<DtoAuthResponse, ApiError>.saveAuthData() {
        if (this is AppResult.Success) {
            local.saveUser(response.user)
            local.saveTokens(response.tokens)
        }
    }
}

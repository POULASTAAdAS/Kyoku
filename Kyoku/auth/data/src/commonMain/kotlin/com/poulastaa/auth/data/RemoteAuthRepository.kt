package com.poulastaa.auth.data

import com.poulastaa.auth.domain.AuthLocalDatasource
import com.poulastaa.auth.domain.AuthRemoteDatasource
import com.poulastaa.auth.domain.AuthRepository
import com.poulastaa.auth.domain.model.DtoAuthResponse
import com.poulastaa.auth.domain.model.DtoForgotPasswordResponse
import com.poulastaa.common.domain.model.DtoSavedScreen
import com.poulastaa.common.domain.model.DtoUser
import com.poulastaa.common.domain.result.ApiError
import com.poulastaa.common.domain.result.AppResult
import com.poulastaa.common.domain.result.asEmptyResponse
import com.poulastaa.common.domain.result.map
import org.koin.core.annotation.Single

// TODO: read the setup screen from the auth response once the server returns it; every user is treated as new for now.
private val DEFAULT_SETUP_SCREEN = DtoSavedScreen.IMPORT_PLAYLIST

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
        if (result is AppResult.Success) {
            local.saveUser(result.response.user)
            local.saveSetupScreen(DEFAULT_SETUP_SCREEN)
        }
        return result.map { it.user.isNewUser }
    }

    override suspend fun signUp(
        email: String,
        username: String,
        password: String,
    ): AppResult<Boolean, ApiError> {
        val result = remote.signUp(email, username, password)
        if (result is AppResult.Success) {
            local.saveUser(result.response.user)
            local.saveSetupScreen(DEFAULT_SETUP_SCREEN)
        }
        return result.map { true }
    }

    override suspend fun checkVerificationStatus(email: String): AppResult<Unit, ApiError> {
        val result = remote.checkVerificationStatus(email)
        if (result is AppResult.Success) local.saveTokens(result.response)

        return result.asEmptyResponse()
    }


    override suspend fun googleAuth(token: String): AppResult<DtoUser, ApiError> {
        val response = remote.googleAuth(token)
        response.saveAuthData()

        return response.map { it.user }
    }

    override suspend fun sendForgotPasswordMail(email: String): AppResult<DtoForgotPasswordResponse, ApiError> =
        remote.sendForgotPasswordMail(email)

    private suspend fun AppResult<DtoAuthResponse, ApiError>.saveAuthData() {
        if (this is AppResult.Success) {
            local.saveUser(response.user)
            local.saveTokens(response.tokens)
            local.saveSetupScreen(DEFAULT_SETUP_SCREEN)
        }
    }
}

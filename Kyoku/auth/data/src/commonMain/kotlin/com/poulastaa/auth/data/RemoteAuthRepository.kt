package com.poulastaa.auth.data

import com.poulastaa.auth.domain.AuthLocalDatasource
import com.poulastaa.auth.domain.AuthRemoteDatasource
import com.poulastaa.auth.domain.AuthRepository
import com.poulastaa.common.network.ApiResult
import com.poulastaa.common.network.EmptyResponse
import com.poulastaa.common.network.Error
import com.poulastaa.common.network.asEmptyResponse
import org.koin.core.annotation.Single

@Single(binds = [AuthRepository::class])
class RemoteAuthRepository(
    private val remote: AuthRemoteDatasource,
    private val local: AuthLocalDatasource,
) : AuthRepository {
    override suspend fun signIn(
        email: String,
        password: String,
    ): EmptyResponse<Error> = when (val response = remote.signIn(email, password)) {
        is ApiResult.Error -> response.asEmptyResponse()
        is ApiResult.Success -> {
            local.saveUser(response.response.user)
            local.saveTokens(response.response.tokens)
        }
    }
}

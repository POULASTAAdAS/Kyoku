package com.poulastaa.auth.data

import com.poulastaa.auth.domain.AuthLocalDatasource
import com.poulastaa.auth.domain.AuthRemoteDatasource
import com.poulastaa.auth.domain.AuthRepository
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
        if (response is ApiResult.Success) {
            local.saveUser(response.response.user)
            local.saveTokens(response.response.tokens)
        }

        return response.map { it.user }
    }
}

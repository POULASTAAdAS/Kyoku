package com.poulastaa.auth.data

import com.poulastaa.auth.domain.AuthLocalDatasource
import com.poulastaa.common.domain.model.DtoTokens
import com.poulastaa.common.domain.model.DtoUser
import com.poulastaa.common.network.AppResult
import com.poulastaa.common.network.DataError
import com.poulastaa.common.network.EmptyResponse
import org.koin.core.annotation.Single

@Single(binds = [AuthLocalDatasource::class])
class RoomDatasource : AuthLocalDatasource {
    override suspend fun saveUser(user: DtoUser): EmptyResponse<DataError> {
        return AppResult.Success(Unit)
    }

    override suspend fun saveTokens(tokens: DtoTokens): EmptyResponse<DataError> {
        return AppResult.Success(Unit)
    }
}
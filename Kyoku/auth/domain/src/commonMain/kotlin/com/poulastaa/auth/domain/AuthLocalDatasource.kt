package com.poulastaa.auth.domain

import com.poulastaa.common.domain.model.DtoTokens
import com.poulastaa.common.domain.model.DtoUser
import com.poulastaa.common.network.DataError
import com.poulastaa.common.network.EmptyResponse

interface AuthLocalDatasource {
    suspend fun saveUser(user: DtoUser) : EmptyResponse<DataError>
    suspend fun saveTokens(tokens: DtoTokens) : EmptyResponse<DataError>
}
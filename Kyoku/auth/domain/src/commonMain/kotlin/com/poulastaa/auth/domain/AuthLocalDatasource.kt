package com.poulastaa.auth.domain

import com.poulastaa.common.domain.model.DtoSavedScreen
import com.poulastaa.common.domain.model.DtoTokens
import com.poulastaa.common.domain.model.DtoUser
import com.poulastaa.common.domain.result.DataError
import com.poulastaa.common.domain.result.EmptyResponse

interface AuthLocalDatasource {
    suspend fun saveUser(user: DtoUser) : EmptyResponse<DataError>
    suspend fun saveTokens(tokens: DtoTokens) : EmptyResponse<DataError>
    suspend fun saveSetupScreen(screen: DtoSavedScreen) : EmptyResponse<DataError>
}
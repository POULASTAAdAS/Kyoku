package com.poulastaa.settings.domain.repository

import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.EmptyResult
import com.poulastaa.core.domain.model.DtoUser

interface SettingRepository {
    suspend fun getUser(): DtoUser
    suspend fun logOut()
    suspend fun deleteAccount(): EmptyResult<DataError.Network>
}
package com.poulastaa.profile.domain.repository

import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.EmptyResult
import com.poulastaa.core.domain.Result

interface RemoteProfileDatasource {
    suspend fun getBDate(): Result<String, DataError.Network>
    suspend fun updateUsername(name: String): EmptyResult<DataError.Network>
}
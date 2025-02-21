package com.poulastaa.main.domain.repository

import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.main.domain.model.DtoHome

interface RemoteHomeDataSource {
    suspend fun getHome(): Result<DtoHome, DataError.Network>
}
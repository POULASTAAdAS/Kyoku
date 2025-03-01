package com.poulastaa.settings.domain.repository

import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result

interface RemoteSettingDatasource {
    suspend fun deleteAccount(): Result<Boolean, DataError.Network>
}
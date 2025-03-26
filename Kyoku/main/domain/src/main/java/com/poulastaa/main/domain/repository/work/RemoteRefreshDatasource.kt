package com.poulastaa.main.domain.repository.work

import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.main.domain.model.DtoRefresh

interface RemoteRefreshDatasource {
    suspend fun refreshSuggestedData(): Result<DtoRefresh, DataError.Network>
}
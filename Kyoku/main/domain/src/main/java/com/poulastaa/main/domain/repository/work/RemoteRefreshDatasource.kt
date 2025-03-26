package com.poulastaa.main.domain.repository.work

import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.main.domain.model.DtoRefresh
import com.poulastaa.main.domain.model.OldRefresh

interface RemoteRefreshDatasource {
    suspend fun refreshSuggestedData(data: OldRefresh): Result<DtoRefresh, DataError.Network>
}
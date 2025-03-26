package com.poulastaa.main.domain.repository.work

import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.EmptyResult

interface RefreshRepository {
    suspend fun refreshSuggestedData(): EmptyResult<DataError.Network>
}
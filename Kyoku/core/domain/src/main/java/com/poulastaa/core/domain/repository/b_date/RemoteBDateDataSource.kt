package com.poulastaa.core.domain.repository.b_date

import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result

interface RemoteBDateDataSource {
    suspend fun storeBDate(bDate: Long): Result<Boolean, DataError.Network>
}
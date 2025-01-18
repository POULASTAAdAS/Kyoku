package com.poulastaa.setup.domain.repository.set_bdate

import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result

interface BDateRepository {
    suspend fun setBDate(bDate: String): Result<Unit, DataError.Network>
}
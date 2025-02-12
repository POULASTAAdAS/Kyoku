package com.poulastaa.setup.domain.repository.set_bdate

import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.setup.domain.model.SetBDateStatus

interface RemoteBDateDatasource {
    suspend fun setBDate(bDate: String): Result<SetBDateStatus, DataError.Network>
}
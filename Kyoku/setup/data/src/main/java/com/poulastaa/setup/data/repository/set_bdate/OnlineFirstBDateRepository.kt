package com.poulastaa.setup.data.repository.set_bdate

import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.asEmptyDataResult
import com.poulastaa.core.domain.map
import com.poulastaa.core.domain.repository.LocalBDateDatasource
import com.poulastaa.setup.domain.model.SetBDateStatus
import com.poulastaa.setup.domain.repository.set_bdate.BDateRepository
import com.poulastaa.setup.domain.repository.set_bdate.RemoteBDateDatasource
import javax.inject.Inject

class OnlineFirstBDateRepository @Inject constructor(
    private val remote: RemoteBDateDatasource,
    private val local: LocalBDateDatasource,
) : BDateRepository {
    override suspend fun setBDate(bDate: String): Result<Unit, DataError.Network> {
        val result = remote.setBDate(bDate)

        result.map {
            if (it == SetBDateStatus.FAILURE) return Result.Error(DataError.Network.UNKNOWN)
            else local.setBDate(bDate)
        }

        return result.asEmptyDataResult()
    }
}
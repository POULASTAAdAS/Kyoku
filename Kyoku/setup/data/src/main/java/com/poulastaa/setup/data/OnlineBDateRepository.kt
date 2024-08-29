package com.poulastaa.setup.data

import com.poulastaa.core.domain.repository.b_date.BDateRepository
import com.poulastaa.core.domain.repository.b_date.RemoteBDateDataSource
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import javax.inject.Inject

class OnlineBDateRepository @Inject constructor(
    private val remote: RemoteBDateDataSource,
    private val applicationScope: CoroutineScope,
) : BDateRepository {
    override suspend fun storeBDate(
        bDate: Long,
    ): Result<Boolean, DataError.Network> = applicationScope.async {
        remote.storeBDate(bDate)
    }.await()
}
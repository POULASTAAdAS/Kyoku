package com.poulastaa.setup.network

import com.google.gson.Gson
import com.poulastaa.core.data.network.put
import com.poulastaa.core.domain.EndPoints
import com.poulastaa.core.domain.repository.b_date.RemoteBDateDataSource
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.setup.network.model.req.StoreBDateReq
import okhttp3.OkHttpClient
import javax.inject.Inject

class BDateRemoteDataSource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
) : RemoteBDateDataSource {
    override suspend fun storeBDate(
        bDate: Long,
    ): Result<Boolean, DataError.Network> = client.put<StoreBDateReq, Boolean>(
        route = EndPoints.StoreBDate.route,
        body = StoreBDateReq(bDate),
        gson = gson,
    )
}
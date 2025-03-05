package com.poulastaa.setup.network.repository

import com.google.gson.Gson
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.map
import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.network.ApiMethodType
import com.poulastaa.core.network.req
import com.poulastaa.setup.domain.model.SetBDateStatus
import com.poulastaa.setup.domain.repository.set_bdate.RemoteBDateDatasource
import com.poulastaa.setup.network.model.SetBDateReq
import com.poulastaa.setup.network.model.SetBDateRes
import com.poulastaa.setup.network.toSetBDateStatus
import okhttp3.OkHttpClient
import javax.inject.Inject

class OkHttpSetBDateDatasource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
) : RemoteBDateDatasource {
    override suspend fun setBDate(bDate: String): Result<SetBDateStatus, DataError.Network> {
        val result = client.req<SetBDateReq, SetBDateRes>(
            route = EndPoints.SetBDate.route,
            method = ApiMethodType.PUT,
            body = SetBDateReq(bDate),
            gson = gson
        )

        return result.map {
            it.status.toSetBDateStatus()
        }
    }
}
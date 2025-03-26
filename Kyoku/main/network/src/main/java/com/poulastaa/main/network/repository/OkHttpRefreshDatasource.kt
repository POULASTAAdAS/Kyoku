package com.poulastaa.main.network.repository

import com.google.gson.Gson
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.map
import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.network.ApiMethodType
import com.poulastaa.core.network.req
import com.poulastaa.main.domain.model.DtoRefresh
import com.poulastaa.main.domain.model.OldRefresh
import com.poulastaa.main.domain.repository.work.RemoteRefreshDatasource
import com.poulastaa.main.network.mapper.toDtoRefresh
import com.poulastaa.main.network.mapper.toRequestRefresh
import com.poulastaa.main.network.model.RequestRefresh
import com.poulastaa.main.network.model.ResponseRefresh
import okhttp3.OkHttpClient
import javax.inject.Inject

internal class OkHttpRefreshDatasource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
) : RemoteRefreshDatasource {
    override suspend fun refreshSuggestedData(data: OldRefresh): Result<DtoRefresh, DataError.Network> =
        client.req<RequestRefresh, ResponseRefresh>(
            route = EndPoints.RefreshHome.route,
            method = ApiMethodType.GET,
            body = data.toRequestRefresh(),
            gson = gson
        ).map { it.toDtoRefresh() }
}
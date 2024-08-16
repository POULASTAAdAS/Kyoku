package com.poulastaa.network

import com.google.gson.Gson
import com.poulastaa.core.data.network.put
import com.poulastaa.core.domain.EndPoints
import com.poulastaa.core.domain.PinReqType
import com.poulastaa.core.domain.library.RemoteLibraryDataSource
import com.poulastaa.core.domain.model.PinnedType
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.asEmptyDataResult
import com.poulastaa.network.model.PinReq
import okhttp3.OkHttpClient
import javax.inject.Inject

class OfflineFirstLibraryDatasource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
) : RemoteLibraryDataSource {
    override suspend fun pinData(
        id: Long,
        pinnedType: PinReqType
    ): EmptyResult<DataError.Network> = client.put<PinReq, Unit>(
        route = EndPoints.PinData.route,
        body = PinReq(
            id = id,
            type = pinnedType
        ),
        gson = gson
    ).asEmptyDataResult()

    override suspend fun unPinData(
        id: Long,
        pinnedType: PinReqType
    ): EmptyResult<DataError.Network> = client.put<PinReq, Unit>(
        route = EndPoints.UnPinData.route,
        body = PinReq(
            id = id,
            type = pinnedType
        ),
        gson = gson
    ).asEmptyDataResult()
}
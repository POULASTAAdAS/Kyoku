package com.poulastaa.profile.netwokr.repository

import com.google.gson.Gson
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.EmptyResult
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.map
import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.network.ApiMethodType
import com.poulastaa.core.network.req
import com.poulastaa.profile.domain.repository.RemoteProfileDatasource
import com.poulastaa.profile.netwokr.model.GetBDateRes
import com.poulastaa.profile.netwokr.model.UpdateUsernameReq
import okhttp3.OkHttpClient
import javax.inject.Inject

class OkHttpProfileDatasource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
) : RemoteProfileDatasource {
    override suspend fun getBDate(): Result<String, DataError.Network> =
        client.req<Unit, GetBDateRes>(
            route = EndPoints.GetBDate.route,
            method = ApiMethodType.GET,
            gson = gson
        ).map {
            it.bDate
        }

    override suspend fun updateUsername(name: String): EmptyResult<DataError.Network> = client.req(
        route = EndPoints.UpdateUsername.route,
        method = ApiMethodType.PUT,
        body = UpdateUsernameReq(name),
        gson = gson
    )
}
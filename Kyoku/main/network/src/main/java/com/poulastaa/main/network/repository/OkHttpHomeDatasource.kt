package com.poulastaa.main.network.repository

import com.google.gson.Gson
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.map
import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.network.ApiMethodType
import com.poulastaa.core.network.req
import com.poulastaa.main.domain.model.DtoHome
import com.poulastaa.main.domain.repository.RemoteHomeDataSource
import com.poulastaa.main.network.mapper.toDtoHome
import com.poulastaa.main.network.model.ResponseHome
import okhttp3.OkHttpClient
import javax.inject.Inject

class OkHttpHomeDatasource @Inject constructor(
    private val gson: Gson,
    private val client: OkHttpClient,
) : RemoteHomeDataSource {
    override suspend fun getHome(): Result<DtoHome, DataError.Network> {
        val result = client.req<Unit, ResponseHome>(
            route = EndPoints.Home.route,
            method = ApiMethodType.GET,
            gson = gson
        )

        return result.map { it.toDtoHome() }
    }
}
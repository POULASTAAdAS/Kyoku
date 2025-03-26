package com.poulastaa.main.network.repository

import com.google.gson.Gson
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.main.domain.model.DtoRefresh
import com.poulastaa.main.domain.repository.work.RemoteRefreshDatasource
import okhttp3.OkHttpClient
import javax.inject.Inject

internal class OkHttpRefreshDatasource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson
) : RemoteRefreshDatasource {
    override suspend fun refreshSuggestedData(): Result<DtoRefresh, DataError.Network> {
        TODO()
    }
}
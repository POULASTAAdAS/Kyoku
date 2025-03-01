package com.poulastaa.settings.network.repository

import com.google.gson.Gson
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.settings.domain.repository.RemoteSettingDatasource
import okhttp3.OkHttpClient
import javax.inject.Inject

class OkHttpRemoteSettingsDatasource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
) : RemoteSettingDatasource {
    override suspend fun deleteAccount(): Result<Boolean, DataError.Network> {
        // todo call delete account

        return Result.Error(DataError.Network.SERVER_ERROR)
    }
}
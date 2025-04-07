package com.poulastaa.view.network.repository

import com.google.gson.Gson
import com.poulastaa.view.domain.repository.RemoteViewSavedItemDatasource
import okhttp3.OkHttpClient
import javax.inject.Inject

internal class OkHttpViewSavedItemDatasource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
) : RemoteViewSavedItemDatasource {
}
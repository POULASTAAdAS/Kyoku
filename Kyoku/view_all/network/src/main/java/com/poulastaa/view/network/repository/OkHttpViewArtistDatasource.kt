package com.poulastaa.view.network.repository

import com.google.gson.Gson
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.map
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.network.ApiMethodType
import com.poulastaa.core.network.ReqParam
import com.poulastaa.core.network.req
import com.poulastaa.view.domain.model.DtoViewArtisPayload
import com.poulastaa.view.domain.repository.RemoteViewArtistDatasource
import com.poulastaa.view.network.mapper.toDtoViewArtist
import com.poulastaa.view.network.model.ViewArtistResponse
import okhttp3.OkHttpClient
import javax.inject.Inject

class OkHttpViewArtistDatasource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
) : RemoteViewArtistDatasource {
    override suspend fun loadArtist(artistId: ArtistId): Result<DtoViewArtisPayload<DtoPrevArtist>, DataError.Network> =
        client.req<Unit, ViewArtistResponse>(
            route = EndPoints.ViewArtist.route,
            method = ApiMethodType.GET,
            params = listOf(
                ReqParam(
                    key = "artistId",
                    value = artistId.toString()
                )
            ),
            gson = gson
        ).map {
            it.toDtoViewArtist()
        }
}
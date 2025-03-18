package com.poulastaa.view.network.repository

import com.google.gson.Gson
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.map
import com.poulastaa.core.domain.model.DtoDetailedPrevSong
import com.poulastaa.core.domain.model.DtoSong
import com.poulastaa.core.domain.model.DtoViewPayload
import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.domain.model.SongId
import com.poulastaa.core.domain.model.ViewType
import com.poulastaa.core.network.ApiMethodType
import com.poulastaa.core.network.ReqParam
import com.poulastaa.core.network.model.ResponseSong
import com.poulastaa.core.network.req
import com.poulastaa.view.domain.repository.RemoteViewOtherDatasource
import com.poulastaa.view.network.mapper.toDtoViewOtherResponse
import com.poulastaa.view.network.mapper.toExploreTypeReq
import com.poulastaa.view.network.model.ResponseDetailPrevSong
import com.poulastaa.view.network.model.ViewOtherResponse
import com.poulastaa.view.network.model.ViewTypeReq
import okhttp3.OkHttpClient
import javax.inject.Inject

class OkHttpViewOtherDatasource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
) : RemoteViewOtherDatasource {
    override suspend fun getFavouriteOrPlaylistViewData(otherId: Long?): Result<DtoViewPayload<DtoSong>, DataError.Network> {
        val params = mutableListOf<ReqParam>()
        if (otherId != null) params.add(
            ReqParam(
                key = "otherId",
                value = otherId.toString()
            )
        )

        params.add(
            ReqParam(
                key = "type",
                value = if (otherId == null) ViewTypeReq.FAVOURITE.name
                else ViewTypeReq.PLAYLIST.name
            )
        )

        return client.req<Unit, ViewOtherResponse<ResponseSong>>(
            route = EndPoints.ViewOther.route,
            method = ApiMethodType.GET,
            params = params.toList(),
            gson = gson
        ).map { it.toDtoViewOtherResponse() }
    }

    override suspend fun getViewAlbum(
        type: ViewType,
        albumId: Long,
    ): Result<DtoViewPayload<DtoDetailedPrevSong>, DataError.Network> =
        client.req<Unit, ViewOtherResponse<ResponseDetailPrevSong>>(
            route = EndPoints.ViewOther.route,
            method = ApiMethodType.GET,
            params = listOf(
                ReqParam(
                    key = "type",
                    value = ViewTypeReq.ALBUM.name
                ),
                ReqParam(
                    key = "otherId",
                    value = albumId.toString()
                )
            ),
            gson = gson
        ).map { it.toDtoViewOtherResponse() }

    override suspend fun getExploreViewData(
        type: ViewType,
        savedSongIdList: List<SongId>,
    ): Result<DtoViewPayload<DtoSong>, DataError.Network> =
        client.req<Unit, ViewOtherResponse<ResponseSong>>(
            route = EndPoints.ViewOther.route,
            method = ApiMethodType.GET,
            params = listOf(
                ReqParam(
                    key = "type",
                    value = type.toExploreTypeReq().name
                ),
                ReqParam(
                    key = "savedSongIdList",
                    value = savedSongIdList.joinToString(",")
                )
            ),
            gson = gson
        ).map { it.toDtoViewOtherResponse() }
}
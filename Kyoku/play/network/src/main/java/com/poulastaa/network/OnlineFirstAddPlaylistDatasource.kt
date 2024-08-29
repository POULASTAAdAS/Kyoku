package com.poulastaa.network

import com.google.gson.Gson
import com.poulastaa.core.data.model.PlaylistDto
import com.poulastaa.core.data.network.post
import com.poulastaa.core.domain.EndPoints
import com.poulastaa.core.domain.ExploreType
import com.poulastaa.core.domain.model.PlaylistData
import com.poulastaa.core.domain.repository.add_playlist.RemoteAddPlaylistDatasource
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.domain.utils.map
import com.poulastaa.network.mapper.toPlaylistData
import com.poulastaa.network.model.SavePlaylistReq
import okhttp3.OkHttpClient
import javax.inject.Inject

class OnlineFirstAddPlaylistDatasource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
) : RemoteAddPlaylistDatasource {
    override suspend fun savePlaylist(
        idList: List<Long>,
        name: String,
        type: ExploreType,
    ): Result<PlaylistData, DataError.Network> = client.post<SavePlaylistReq, PlaylistDto>(
        route = EndPoints.SavePlaylist.route,
        body = SavePlaylistReq(
            idList = idList,
            name = name,
            type = type.name
        ),
        gson = gson
    ).map { it.toPlaylistData() }
}
package com.poulastaa.core.network.repository

import com.google.gson.Gson
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.map
import com.poulastaa.core.domain.model.DtoPlaylist
import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.domain.repository.RemoteCreatePlaylistDatasource
import com.poulastaa.core.network.ApiMethodType
import com.poulastaa.core.network.mapper.toDtoPlaylist
import com.poulastaa.core.network.model.CreatePlaylistRequest
import com.poulastaa.core.network.model.ResponsePlaylist
import com.poulastaa.core.network.req
import kotlinx.serialization.InternalSerializationApi
import okhttp3.OkHttpClient
import javax.inject.Inject

@OptIn(InternalSerializationApi::class)
internal class OkHttpCratePlaylistDatasource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
) : RemoteCreatePlaylistDatasource {
    override suspend fun createPlaylist(
        name: String,
    ): Result<DtoPlaylist, DataError.Network> = client.req<CreatePlaylistRequest, ResponsePlaylist>(
        route = EndPoints.Playlist.CreatePlaylist.route,
        method = ApiMethodType.POST,
        body = CreatePlaylistRequest(
            playlistName = name
        ),
        gson = gson
    ).map { it.toDtoPlaylist() }
}
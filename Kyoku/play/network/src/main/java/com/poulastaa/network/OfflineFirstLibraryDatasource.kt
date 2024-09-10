package com.poulastaa.network

import com.google.gson.Gson
import com.poulastaa.core.data.model.PlaylistDto
import com.poulastaa.core.data.network.get
import com.poulastaa.core.data.network.post
import com.poulastaa.core.data.network.put
import com.poulastaa.core.domain.EndPoints
import com.poulastaa.core.domain.LibraryDataType
import com.poulastaa.core.domain.model.Playlist
import com.poulastaa.core.domain.repository.library.RemoteLibraryDataSource
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.domain.utils.asEmptyDataResult
import com.poulastaa.core.domain.utils.map
import com.poulastaa.network.mapper.toPlaylistWithSong
import com.poulastaa.network.model.CreatePlaylistWithSongReq
import com.poulastaa.network.model.PinReq
import okhttp3.OkHttpClient
import javax.inject.Inject

class OfflineFirstLibraryDatasource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
) : RemoteLibraryDataSource {
    override suspend fun pinData(
        id: Long,
        type: LibraryDataType
    ): EmptyResult<DataError.Network> = client.put<PinReq, Unit>(
        route = EndPoints.PinData.route,
        body = PinReq(
            id = id,
            type = type
        ),
        gson = gson
    ).asEmptyDataResult()

    override suspend fun unPinData(
        id: Long,
        type: LibraryDataType
    ): EmptyResult<DataError.Network> = client.put<PinReq, Unit>(
        route = EndPoints.UnPinData.route,
        body = PinReq(
            id = id,
            type = type
        ),
        gson = gson
    ).asEmptyDataResult()

    override suspend fun deleteSavedData(
        id: Long,
        type: LibraryDataType
    ): EmptyResult<DataError.Network> = client.get<Unit>(
        route = EndPoints.DeleteSavedData.route,
        params = listOf(
            Pair("id", id.toString()),
            Pair("type", type.name)
        ),
        gson = gson
    ).asEmptyDataResult()

    override suspend fun createPlaylist(name: String): Result<Playlist, DataError.Network> =
        client.post<CreatePlaylistWithSongReq, PlaylistDto>(
            route = EndPoints.CreatePlaylist.route,
            body = CreatePlaylistWithSongReq(
                name = name,
                songId = -1
            ),
            gson = gson
        ).map { it.toPlaylistWithSong().playlist }
}
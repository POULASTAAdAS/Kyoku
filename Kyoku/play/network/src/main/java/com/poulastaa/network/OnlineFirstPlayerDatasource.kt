package com.poulastaa.network

import com.google.gson.Gson
import com.poulastaa.core.data.model.AlbumWithSongDto
import com.poulastaa.core.data.model.PlaylistDto
import com.poulastaa.core.data.network.get
import com.poulastaa.core.data.network.post
import com.poulastaa.core.domain.EndPoints
import com.poulastaa.core.domain.RecentHistoryOtherType
import com.poulastaa.core.domain.model.AlbumWithSong
import com.poulastaa.core.domain.model.PlaylistData
import com.poulastaa.core.domain.model.SongOtherData
import com.poulastaa.core.domain.repository.player.RemotePlayerDatasource
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.domain.utils.asEmptyDataResult
import com.poulastaa.core.domain.utils.map
import com.poulastaa.network.mapper.toAlbumWithSong
import com.poulastaa.network.mapper.toHistoryTypeDto
import com.poulastaa.network.mapper.toPlaylistData
import com.poulastaa.network.mapper.toSongOtherData
import com.poulastaa.network.model.GetDataReq
import com.poulastaa.network.model.GetDataType
import com.poulastaa.network.model.HistoryReq
import com.poulastaa.network.model.SongOtherDto
import okhttp3.OkHttpClient
import javax.inject.Inject

class OnlineFirstPlayerDatasource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
) : RemotePlayerDatasource {
    override suspend fun getAlbum(
        id: Long,
    ): Result<AlbumWithSong, DataError.Network> = client.post<GetDataReq, AlbumWithSongDto>(
        route = EndPoints.GetTypeData.route,
        body = GetDataReq(
            id = id,
            type = GetDataType.ALBUM
        ),
        gson = gson
    ).map {
        it.toAlbumWithSong()
    }

    override suspend fun getPlaylist(id: Long): Result<PlaylistData, DataError.Network> =
        client.post<GetDataReq, PlaylistDto>(
            route = EndPoints.GetTypeData.route,
            body = GetDataReq(
                id = id,
                type = GetDataType.PLAYLIST
            ),
            gson = gson
        ).map {
            it.toPlaylistData()
        }

    override suspend fun loadOtherInfo(songId: Long): Result<SongOtherData, DataError.Network> =
        client.get<SongOtherDto>(
            route = EndPoints.GetSongOtherData.route,
            params = listOf("songId" to songId.toString()),
            gson = gson
        ).map { it.toSongOtherData() }

    override suspend fun addSongToHistory(
        songId: Long,
        otherId: Long,
        type: RecentHistoryOtherType
    ): EmptyResult<DataError.Network> = client.post<HistoryReq, Unit>(
        route = EndPoints.UpdateHistory.route,
        body = HistoryReq(
            songId = songId,
            otherId = otherId,
            type = type.toHistoryTypeDto()
        ),
        gson = gson
    ).asEmptyDataResult()
}
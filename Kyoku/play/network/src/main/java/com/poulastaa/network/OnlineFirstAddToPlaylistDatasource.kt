package com.poulastaa.network

import com.google.gson.Gson
import com.poulastaa.core.data.model.PlaylistDto
import com.poulastaa.core.data.model.SongDto
import com.poulastaa.core.data.network.get
import com.poulastaa.core.data.network.post
import com.poulastaa.core.domain.EndPoints
import com.poulastaa.core.domain.model.PlaylistData
import com.poulastaa.core.domain.model.Song
import com.poulastaa.core.domain.repository.add_to_playlist.RemoteAddToPlaylistDatasource
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.domain.utils.map
import com.poulastaa.network.mapper.toPlaylistData
import com.poulastaa.network.mapper.toSong
import com.poulastaa.network.model.CreatePlaylistWithSongReq
import com.poulastaa.network.model.UpdateFavouriteReq
import com.poulastaa.network.model.UpdatePlaylistReq
import okhttp3.OkHttpClient
import javax.inject.Inject

class OnlineFirstAddToPlaylistDatasource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson
) : RemoteAddToPlaylistDatasource {
    override suspend fun saveSong(
        songId: Long
    ): Result<Song, DataError.Network> = client.get<SongDto>(
        route = EndPoints.GetSong.route,
        params = listOf(
            Pair(
                first = "songId",
                second = songId.toString()
            )
        ),
        gson = gson
    ).map { it.toSong() }

    override suspend fun editPlaylist(
        songId: Long,
        playlistIdList: Map<Long, Boolean>,
    ): EmptyResult<DataError.Network> = client.post<UpdatePlaylistReq, Unit>(
        route = EndPoints.UpdatePlaylist.route,
        body = UpdatePlaylistReq(
            songId = songId,
            playlistIdList = playlistIdList
        ),
        gson = gson
    )

    override suspend fun addSongToFavourite(
        songId: Long
    ): EmptyResult<DataError.Network> = client.post<UpdateFavouriteReq, Unit>(
        route = EndPoints.UpdateFavourite.route,
        body = UpdateFavouriteReq(
            songId = songId,
            opp = true
        ),
        gson = gson
    )

    override suspend fun removeSongToFavourite(
        songId: Long
    ): EmptyResult<DataError.Network> = client.post<UpdateFavouriteReq, Unit>(
        route = EndPoints.UpdateFavourite.route,
        body = UpdateFavouriteReq(
            songId = songId,
            opp = false
        ),
        gson = gson
    )

    override suspend fun createPlaylist(
        songId: Long,
        name: String
    ): Result<PlaylistData, DataError.Network> =
        client.post<CreatePlaylistWithSongReq, PlaylistDto>(
            route = EndPoints.CreatePlaylist.route,
            body = CreatePlaylistWithSongReq(
                name = name,
                songId = songId
            ),
            gson = gson
        ).map { it.toPlaylistData() }
}
package com.poulastaa.network

import com.google.gson.Gson
import com.poulastaa.core.data.model.SongDto
import com.poulastaa.core.data.network.get
import com.poulastaa.core.data.network.post
import com.poulastaa.core.domain.EndPoints
import com.poulastaa.core.domain.model.Song
import com.poulastaa.core.domain.repository.view_edit.RemoteViewEditDatasource
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.domain.utils.map
import com.poulastaa.network.mapper.toSong
import com.poulastaa.network.model.UpdatePlaylistReq
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import okhttp3.OkHttpClient
import javax.inject.Inject

class OnlineFirstEditViewDatasource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
) : RemoteViewEditDatasource {
    override suspend fun deleteSong(
        playlistId: Long,
        songId: Long,
    ): EmptyResult<DataError.Network> = client.post<UpdatePlaylistReq, Unit>(
        route = EndPoints.UpdatePlaylist.route,
        body = UpdatePlaylistReq(
            songId = songId,
            playlistIdList = mapOf(playlistId to false)
        ),
        gson = gson
    )

    override suspend fun addSong(playlistId: Long, songId: Long): Result<Song, DataError.Network> =
        coroutineScope {
            val result = async {
                client.post<UpdatePlaylistReq, Unit>(
                    route = EndPoints.UpdatePlaylist.route,
                    body = UpdatePlaylistReq(
                        songId = songId,
                        playlistIdList = mapOf(playlistId to true)
                    ),
                    gson = gson
                )
            }

            val song = async {
                client.get<SongDto>(
                    route = EndPoints.GetSong.route,
                    params = listOf(
                        Pair(
                            first = "songId",
                            second = songId.toString()
                        )
                    ),
                    gson = gson
                )
            }

            result.await()
            song.await().map {
                it.toSong()
            }
        }
}
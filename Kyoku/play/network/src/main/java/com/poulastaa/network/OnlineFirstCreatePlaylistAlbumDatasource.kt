package com.poulastaa.network

import com.google.gson.Gson
import com.poulastaa.core.data.model.AlbumWithSongDto
import com.poulastaa.core.data.network.get
import com.poulastaa.core.domain.EndPoints
import com.poulastaa.core.domain.model.AlbumWithSong
import com.poulastaa.core.domain.repository.create_playlist.album.RemoteCreatePlaylistAlbumDatasource
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.domain.utils.map
import com.poulastaa.network.mapper.toAlbumWithSong
import okhttp3.OkHttpClient
import javax.inject.Inject

class OnlineFirstCreatePlaylistAlbumDatasource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson
) : RemoteCreatePlaylistAlbumDatasource {
    override suspend fun getAlbum(
        albumId: Long,
        savedSongIdList: List<Long>
    ): Result<AlbumWithSong, DataError.Network> = client.get<AlbumWithSongDto>(
        route = EndPoints.GetAlbum.route,
        params = listOf(
            "albumId" to albumId.toString(),
            "savedSongIdList" to savedSongIdList.joinToString(separator = ",")
        ),
        gson = gson
    ).map {
        it.toAlbumWithSong()
    }
}
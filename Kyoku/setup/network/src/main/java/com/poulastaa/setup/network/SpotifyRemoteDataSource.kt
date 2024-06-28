package com.poulastaa.setup.network

import com.google.gson.Gson
import com.poulastaa.core.data.network.post
import com.poulastaa.core.domain.EndPoints
import com.poulastaa.core.domain.get_spotify_playlist.RemoteSpotifyDataSource
import com.poulastaa.core.domain.model.PlaylistWithSong
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.domain.utils.map
import com.poulastaa.setup.network.mappers.toPlaylistWithSong
import com.poulastaa.setup.network.model.req.CreatePlaylistReq
import com.poulastaa.setup.network.model.res.PlaylistDto
import okhttp3.OkHttpClient
import javax.inject.Inject

class SpotifyRemoteDataSource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
) : RemoteSpotifyDataSource {
    override suspend fun createPlaylist(
        url: String,
    ): Result<PlaylistWithSong, DataError.Network> = client.post<CreatePlaylistReq, PlaylistDto>(
        route = EndPoints.GetSpotifyPlaylistSong.route,
        body = CreatePlaylistReq(url),
        gson = gson,
    ).map {
        it.toPlaylistWithSong()
    }
}
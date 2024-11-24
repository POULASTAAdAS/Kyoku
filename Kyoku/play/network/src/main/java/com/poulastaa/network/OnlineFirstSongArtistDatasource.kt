package com.poulastaa.network

import com.google.gson.Gson
import com.poulastaa.core.data.network.get
import com.poulastaa.core.domain.EndPoints
import com.poulastaa.core.domain.model.ArtistWithPopularity
import com.poulastaa.core.domain.repository.song_artist.RemoteSongArtistDatasource
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.domain.utils.map
import com.poulastaa.network.mapper.toArtistWithPopularity
import okhttp3.OkHttpClient
import javax.inject.Inject


class OnlineFirstSongArtistDatasource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
) : RemoteSongArtistDatasource {
    override suspend fun getArtistOnSongId(songId: Long): Result<List<ArtistWithPopularity>, DataError.Network> =
        client.get<SongArtistRes>(
            route = EndPoints.GetSongArtist.route,
            params = listOf(
                "songId" to songId.toString()
            ),
            gson = gson
        ).map {
            it.list.map { dto -> dto.toArtistWithPopularity() }
        }
}
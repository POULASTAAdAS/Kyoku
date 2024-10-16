package com.poulastaa.network

import com.google.gson.Gson
import com.poulastaa.core.data.model.SongDto
import com.poulastaa.core.data.network.get
import com.poulastaa.core.data.network.post
import com.poulastaa.core.domain.EndPoints
import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.domain.model.Song
import com.poulastaa.core.domain.model.ViewArtistData
import com.poulastaa.core.domain.repository.view_artist.RemoteViewArtistDatasource
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.domain.utils.asEmptyDataResult
import com.poulastaa.core.domain.utils.map
import com.poulastaa.network.mapper.toArtist
import com.poulastaa.network.mapper.toSong
import com.poulastaa.network.mapper.toViewArtistData
import com.poulastaa.network.model.AddArtistDto
import com.poulastaa.network.model.AddArtistReq
import com.poulastaa.network.model.ViewArtistDto
import okhttp3.OkHttpClient
import javax.inject.Inject

class OnlineFirstViewArtistDatasource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
) : RemoteViewArtistDatasource {
    override suspend fun getData(
        artistId: Long,
    ): Result<ViewArtistData, DataError.Network> = client.get<ViewArtistDto>(
        route = EndPoints.ViewArtist.route,
        params = listOf("artistId" to artistId.toString()),
        gson = gson
    ).map { it.toViewArtistData() }

    override suspend fun followArtist(
        artistId: Long,
    ): Result<Artist, DataError.Network> = client.post<AddArtistReq, AddArtistDto>(
        route = EndPoints.AddArtist.route,
        body = AddArtistReq(listOf(artistId)),
        gson = gson
    ).map {
        it.list.first().toArtist()
    }

    override suspend fun unFollowArtist(
        artistId: Long,
    ): EmptyResult<DataError.Network> = client.get<Unit>(
        route = EndPoints.RemoveArtist.route,
        params = listOf("artistId" to artistId.toString()),
        gson = gson
    ).asEmptyDataResult()

    override suspend fun addSongToFavourite(
        songId: Long,
    ): Result<Song, DataError.Network> = client.get<SongDto>(
        route = EndPoints.AddToFavourite.route,
        params = listOf("songId" to songId.toString()),
        gson = gson
    ).map {
        it.toSong()
    }
}
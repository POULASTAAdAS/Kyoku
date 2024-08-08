package com.poulastaa.network

import com.google.gson.Gson
import com.poulastaa.core.data.model.AlbumWithSongDto
import com.poulastaa.core.data.model.ArtistDto
import com.poulastaa.core.data.model.SongDto
import com.poulastaa.core.data.network.get
import com.poulastaa.core.data.network.post
import com.poulastaa.core.domain.EndPoints
import com.poulastaa.core.domain.home.RemoteHomeDatasource
import com.poulastaa.core.domain.model.AlbumWithSong
import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.domain.model.DayType
import com.poulastaa.core.domain.model.NewHome
import com.poulastaa.core.domain.model.Song
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.domain.utils.asEmptyDataResult
import com.poulastaa.core.domain.utils.map
import com.poulastaa.network.mapper.toAlbumWithSong
import com.poulastaa.network.mapper.toArtist
import com.poulastaa.network.mapper.toNewHome
import com.poulastaa.network.mapper.toSong
import com.poulastaa.network.model.NewHomeDto
import com.poulastaa.network.model.NewHomeReq
import okhttp3.OkHttpClient
import javax.inject.Inject

class OnlineFirstHomeDatasource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
) : RemoteHomeDatasource {
    override suspend fun getNewHomeResponse(
        dayType: DayType,
    ): Result<NewHome, DataError.Network> = client.post<NewHomeReq, NewHomeDto>(
        route = EndPoints.NewHome.route,
        body = NewHomeReq(
            dayType = dayType
        ),
        gson = gson
    ).map {
        it.toNewHome()
    }

    override suspend fun insertIntoFavourite(
        id: Long,
    ): Result<Song, DataError.Network> = client.get<SongDto>(
        route = EndPoints.AddToFavourite.route,
        params = listOf(
            "songId" to id.toString(),
        ),
        gson = gson
    ).map {
        it.toSong()
    }

    override suspend fun removeFromFavourite(
        id: Long,
    ): EmptyResult<DataError.Network> = client.get<Unit>(
        route = EndPoints.RemoveFromFavourite.route,
        params = listOf(
            "songId" to id.toString(),
        ),
        gson = gson
    ).asEmptyDataResult()

    override suspend fun followArtist(
        id: Long,
    ): Result<Artist, DataError.Network> = client.get<ArtistDto>(
        route = EndPoints.AddArtist.route,
        params = listOf(
            "artistId" to id.toString(),
        ),
        gson = gson
    ).map { it.toArtist() }

    override suspend fun unFollowArtist(
        id: Long,
    ): EmptyResult<DataError.Network> = client.get<Unit>(
        route = EndPoints.RemoveArtist.route,
        params = listOf(
            "artistId" to id.toString(),
        ),
        gson = gson
    ).asEmptyDataResult()

    override suspend fun saveAlbum(id: Long): Result<AlbumWithSong, DataError.Network> =
        client.get<AlbumWithSongDto>(
            route = EndPoints.AddAlbum.route,
            params = listOf(
                "albumId" to id.toString(),
            ),
            gson = gson
        ).map { it.toAlbumWithSong() }

    override suspend fun removeAlbum(id: Long): EmptyResult<DataError.Network> = client.get<Unit>(
        route = EndPoints.RemoveAlbum.route,
        params = listOf(
            "albumId" to id.toString(),
        ),
        gson = gson
    ).asEmptyDataResult()
}
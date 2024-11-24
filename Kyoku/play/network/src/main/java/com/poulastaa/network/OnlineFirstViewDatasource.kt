package com.poulastaa.network

import com.google.gson.Gson
import com.poulastaa.core.data.model.AlbumWithSongDto
import com.poulastaa.core.data.model.PlaylistDto
import com.poulastaa.core.data.model.SongDto
import com.poulastaa.core.data.network.get
import com.poulastaa.core.data.network.post
import com.poulastaa.core.domain.EndPoints
import com.poulastaa.core.domain.model.AlbumWithSong
import com.poulastaa.core.domain.model.PlaylistData
import com.poulastaa.core.domain.model.Song
import com.poulastaa.core.domain.repository.view.RemoteViewDatasource
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.domain.utils.map
import com.poulastaa.network.mapper.toAlbumWithSong
import com.poulastaa.network.mapper.toPlaylistData
import com.poulastaa.network.mapper.toSong
import com.poulastaa.network.model.GetDataReq
import com.poulastaa.network.model.GetDataType
import okhttp3.OkHttpClient
import javax.inject.Inject

class OnlineFirstViewDatasource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
) : RemoteViewDatasource {
    override suspend fun getPlaylistOnId(
        id: Long,
    ): Result<PlaylistData, DataError.Network> = client.post<GetDataReq, PlaylistDto>(
        route = EndPoints.GetTypeData.route,
        body = GetDataReq(
            id = id,
            type = GetDataType.PLAYLIST
        ),
        gson = gson
    ).map {
        it.toPlaylistData()
    }

    override suspend fun getAlbumOnId(
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

    override suspend fun getFev(): Result<List<Song>, DataError.Network> =
        client.post<GetDataReq, List<SongDto>>(
            route = EndPoints.GetTypeData.route,
            body = GetDataReq(type = GetDataType.FEV),
            gson = gson
        ).map {
            it.map { dto -> dto.toSong() }
        }

    override suspend fun getOldMix(
        prevList: List<Long>,
    ): Result<List<Song>, DataError.Network> = client.post<GetDataReq, PlaylistDto>(
        route = EndPoints.GetTypeData.route,
        body = GetDataReq(
            listOfId = prevList,
            type = GetDataType.OLD_MIX
        ),
        gson = gson
    ).map {
        it.listOfSong.map { dto -> dto.toSong() }
    }

    override suspend fun getArtistMix(
        prevList: List<Long>,
    ): Result<List<Song>, DataError.Network> = client.post<GetDataReq, PlaylistDto>(
        route = EndPoints.GetTypeData.route,
        body = GetDataReq(
            listOfId = prevList,
            type = GetDataType.ARTIST_MIX
        ),
        gson = gson
    ).map {
        it.listOfSong.map { dto -> dto.toSong() }
    }

    override suspend fun getPopularMix(
        prevList: List<Long>,
    ): Result<List<Song>, DataError.Network> = client.post<GetDataReq, PlaylistDto>(
        route = EndPoints.GetTypeData.route,
        body = GetDataReq(
            listOfId = prevList,
            type = GetDataType.POPULAR_MIX
        ),
        gson = gson
    ).map {
        it.listOfSong.map { dto -> dto.toSong() }
    }

    override suspend fun getSongOnIdList(
        list: List<Long>,
    ): Result<List<Song>, DataError.Network> = throw Exception("not implemented")


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
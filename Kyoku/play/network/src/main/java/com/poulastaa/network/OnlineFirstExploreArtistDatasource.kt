package com.poulastaa.network

import com.google.gson.Gson
import com.poulastaa.core.data.model.ArtistDto
import com.poulastaa.core.data.network.get
import com.poulastaa.core.domain.EndPoints
import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.domain.model.ArtistSingleData
import com.poulastaa.core.domain.repository.explore_artist.RemoteExploreArtistDatasource
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.domain.utils.asEmptyDataResult
import com.poulastaa.core.domain.utils.map
import com.poulastaa.network.mapper.toArtist
import com.poulastaa.network.mapper.toArtistSingleData
import com.poulastaa.network.model.ArtistPagerDataDto
import okhttp3.OkHttpClient
import javax.inject.Inject

class OnlineFirstExploreArtistDatasource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson
) : RemoteExploreArtistDatasource {
    override suspend fun getArtist(
        artistId: Long
    ): Result<Artist, DataError.Network> = client.get<ArtistDto>(
        route = EndPoints.GetArtist.route,
        params = listOf("artistId" to artistId.toString()),
        gson = gson
    ).map { it.toArtist() }

    override suspend fun followArtist(
        artistId: Long
    ): Result<Artist, DataError.Network> = client.get<ArtistDto>(
        route = EndPoints.AddArtist.route,
        params = listOf("artistId" to artistId.toString()),
        gson = gson
    ).map { it.toArtist() }

    override suspend fun unFollowArtist(
        artistId: Long
    ): EmptyResult<DataError.Network> = client.get<Unit>(
        route = EndPoints.RemoveArtist.route,
        params = listOf("artistId" to artistId.toString()),
        gson = gson
    ).asEmptyDataResult()

    override suspend fun getArtistSong(
        artistId: Long,
        page: Int,
        size: Long
    ): Result<List<ArtistSingleData>, DataError.Network> = client.get<ArtistPagerDataDto>(
        route = EndPoints.GetArtistSong.route,
        params = listOf(
            "page" to page.toString(),
            "size" to 10.toString(),
            "artistId" to artistId.toString()
        ),
        gson = gson
    ).map {
        it.list.map { dto -> dto.toArtistSingleData() }
    }

    override suspend fun getArtistAlbum(
        artistId: Long,
        page: Int,
        size: Long
    ): Result<List<ArtistSingleData>, DataError.Network> = client.get<ArtistPagerDataDto>(
        route = EndPoints.GetArtistAlbum.route,
        params = listOf(
            "page" to page.toString(),
            "size" to 10.toString(),
            "artistId" to artistId.toString()
        ),
        gson = gson
    ).map {
        it.list.map { dto -> dto.toArtistSingleData() }
    }
}
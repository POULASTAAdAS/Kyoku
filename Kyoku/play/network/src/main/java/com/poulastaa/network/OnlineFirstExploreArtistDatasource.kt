package com.poulastaa.network

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
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
import com.poulastaa.paging_source.ExploreArtistAlbumPagerSource
import com.poulastaa.paging_source.ExploreArtistSongPagerSource
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import javax.inject.Inject

class OnlineFirstExploreArtistDatasource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
    private val pagerAlbum: ExploreArtistAlbumPagerSource,
    private val pagerSong: ExploreArtistSongPagerSource,
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

    override suspend fun getArtistSong(artistId: Long): Flow<PagingData<ArtistSingleData>> {
        pagerSong.init(artistId)

        return Pager(
            config = PagingConfig(
                pageSize = 10,
            ),
            initialKey = 1,
            pagingSourceFactory = { pagerSong }
        ).flow
    }

    override suspend fun getArtistAlbum(artistId: Long): Flow<PagingData<ArtistSingleData>> {
        pagerAlbum.init(artistId)

        return Pager(
            config = PagingConfig(
                pageSize = 10,
            ),
            initialKey = 1,
            pagingSourceFactory = { pagerAlbum }
        ).flow
    }
}
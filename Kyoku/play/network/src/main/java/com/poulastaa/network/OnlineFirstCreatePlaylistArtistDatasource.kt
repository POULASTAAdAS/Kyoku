package com.poulastaa.network

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.google.gson.Gson
import com.poulastaa.core.data.model.ArtistDto
import com.poulastaa.core.data.network.get
import com.poulastaa.core.domain.EndPoints
import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.domain.model.CreatePlaylistPagingData
import com.poulastaa.core.domain.repository.create_playlist.artist.RemoteCreatePlaylistArtistDatasource
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.domain.utils.map
import com.poulastaa.network.mapper.toArtist
import com.poulastaa.network.mapper.toCreatePlaylistPagingData
import com.poulastaa.network.paging_source.ExploreArtistAlbumPagerSource
import com.poulastaa.network.paging_source.ExploreArtistSongPagerSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.OkHttpClient
import javax.inject.Inject

class OnlineFirstCreatePlaylistArtistDatasource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
    private val album: ExploreArtistAlbumPagerSource,
    private val song: ExploreArtistSongPagerSource
) : RemoteCreatePlaylistArtistDatasource {
    override suspend fun getArtist(artistId: Long): Result<Artist, DataError.Network> =
        client.get<ArtistDto>(
            route = EndPoints.GetArtist.route,
            params = listOf("artistId" to artistId.toString()),
            gson = gson
        ).map { it.toArtist() }

    override suspend fun getPagingAlbum(
        artistId: Long,
    ): Flow<PagingData<CreatePlaylistPagingData>> {
        album.init(artistId)

        return Pager(
            config = PagingConfig(
                pageSize = 15,
            ),
            initialKey = 1,
            pagingSourceFactory = { album }
        ).flow.map { pagingData ->
            pagingData.map { it.toCreatePlaylistPagingData() }
        }
    }

    override suspend fun getPagingSong(
        artistId: Long,
        savedSongIdList: List<Long>
    ): Flow<PagingData<CreatePlaylistPagingData>> {
        song.init(artistId, savedSongIdList)

        return Pager(
            config = PagingConfig(
                pageSize = 10,
            ),
            initialKey = 1,
            pagingSourceFactory = { song }
        ).flow.map { pagingData ->
            pagingData.map { it.toCreatePlaylistPagingData() }
        }
    }
}
package com.poulastaa.explore.network.repository.all_from_artist

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.Gson
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.map
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.network.ApiMethodType
import com.poulastaa.core.network.ReqParam
import com.poulastaa.core.network.model.ResponseArtist
import com.poulastaa.core.network.req
import com.poulastaa.explore.domain.model.DtoExploreItem
import com.poulastaa.explore.domain.repository.all_from_artist.RemoteAllFromArtistDatasource
import com.poulastaa.explore.network.mapper.toDtoPrevArtist
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import javax.inject.Inject

internal class OkHttpAllFromArtistDatasource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
    private val song: AllFromArtistSongPagingSource,
    private val album: AllFromArtistAlbumPagingSource,
) : RemoteAllFromArtistDatasource {
    override suspend fun getArtist(artistId: ArtistId): Result<DtoPrevArtist, DataError.Network> =
        client.req<Unit, ResponseArtist>(
            route = EndPoints.Artist.GetArtist.route,
            method = ApiMethodType.GET,
            params = listOf(
                ReqParam(
                    key = "artistId",
                    value = artistId.toString()
                )
            ),
            gson = gson
        ).map { it.toDtoPrevArtist() }

    override fun getSongs(
        artistId: ArtistId,
        query: String,
    ): Flow<PagingData<DtoExploreItem>> {
        song.init(artistId, query)

        return Pager(
            config = PagingConfig(pageSize = 10, initialLoadSize = 15),
            initialKey = 1,
            pagingSourceFactory = { song }
        ).flow
    }

    override fun getAlbums(
        artistId: ArtistId,
        query: String,
    ): Flow<PagingData<DtoExploreItem>> {
        album.init(artistId, query)

        return Pager(
            config = PagingConfig(pageSize = 10, initialLoadSize = 15),
            initialKey = 1,
            pagingSourceFactory = { album }
        ).flow
    }
}
package com.poulastaa.network

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.Gson
import com.poulastaa.core.data.network.post
import com.poulastaa.core.domain.EndPoints
import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.domain.model.ArtistPagingType
import com.poulastaa.core.domain.repository.new_artist.RemoteNewArtistDataSource
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.domain.utils.map
import com.poulastaa.network.mapper.toArtist
import com.poulastaa.network.model.AddArtistDto
import com.poulastaa.network.model.AddArtistReq
import com.poulastaa.network.paging_source.NewArtistPagerSource
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import javax.inject.Inject

class OnlineFirstNewArtistDatasource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
    private val pager: NewArtistPagerSource
) : RemoteNewArtistDataSource {
    override fun getPagingArtist(
        query: String,
        type: ArtistPagingType,
    ): Flow<PagingData<Artist>> {
        pager.init(
            query = query,
            type = type,
        )

        return Pager(
            config = PagingConfig(
                pageSize = 10,
            ),
            initialKey = 1,
            pagingSourceFactory = { pager }
        ).flow
    }

    override suspend fun saveArtist(list: List<Long>): Result<List<Artist>, DataError.Network> =
        client.post<AddArtistReq, AddArtistDto>(
            route = EndPoints.AddArtist.route,
            body = AddArtistReq(
                list = list
            ),
            gson = gson
        ).map {
            it.list.map { dto -> dto.toArtist() }
        }
}
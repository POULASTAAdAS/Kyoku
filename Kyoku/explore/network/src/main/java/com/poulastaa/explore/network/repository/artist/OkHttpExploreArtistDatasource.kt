package com.poulastaa.explore.network.repository.artist

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.Gson
import com.poulastaa.explore.domain.model.DtoExploreArtistFilterType
import com.poulastaa.explore.domain.model.DtoExploreItem
import com.poulastaa.explore.domain.repository.artist.RemoteExploreArtistDatasource
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import javax.inject.Inject

internal class OkHttpExploreArtistDatasource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
    private val artist: ExploreArtistPagingSource,
) : RemoteExploreArtistDatasource {
    override fun getArtist(
        query: String,
        filterType: DtoExploreArtistFilterType,
    ): Flow<PagingData<DtoExploreItem>> {
        artist.init(query, filterType)

        return Pager(
            config = PagingConfig(pageSize = 10, initialLoadSize = 15),
            initialKey = 1,
            pagingSourceFactory = { artist }
        ).flow
    }
}
package com.poulastaa.explore.network.repository.search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.Gson
import com.poulastaa.explore.domain.model.DtoExploreAlbumFilterType
import com.poulastaa.explore.domain.model.DtoExploreItem
import com.poulastaa.explore.domain.repository.album.RemoteExploreAlbumDatasource
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import javax.inject.Inject

internal class OkHttpExploreAlbumDatasource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
    private val album: ExploreAlbumPagingSource,
) : RemoteExploreAlbumDatasource {
    override suspend fun getAlbum(
        query: String,
        filterType: DtoExploreAlbumFilterType,
    ): Flow<PagingData<DtoExploreItem>> {
        album.init(query, filterType)

        return Pager(
            config = PagingConfig(pageSize = 10, initialLoadSize = 15),
            initialKey = 1,
            pagingSourceFactory = { album }
        ).flow
    }
}
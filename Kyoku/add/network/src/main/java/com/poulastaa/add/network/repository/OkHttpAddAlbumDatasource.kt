package com.poulastaa.add.network.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.Gson
import com.poulastaa.add.domain.model.DtoAddAlbum
import com.poulastaa.add.domain.model.DtoAddAlbumSearchFilterType
import com.poulastaa.add.domain.repository.RemoteAddAlbumDatasource
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import javax.inject.Inject

internal class OkHttpAddAlbumDatasource @Inject constructor(
    private val album: AddAlbumPagingSource,
    private val client: OkHttpClient,
    private val gson: Gson,
) : RemoteAddAlbumDatasource {
    override suspend fun loadAlbum(
        query: String,
        filterType: DtoAddAlbumSearchFilterType,
    ): Flow<PagingData<DtoAddAlbum>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            initialLoadSize = 15,
        ),
        pagingSourceFactory = {
            album.init(query, filterType)
        },
    ).flow


}
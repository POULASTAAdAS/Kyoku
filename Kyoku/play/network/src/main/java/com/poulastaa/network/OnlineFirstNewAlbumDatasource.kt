package com.poulastaa.network

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.Gson
import com.poulastaa.core.domain.model.AlbumPagingType
import com.poulastaa.core.domain.model.AlbumWithSong
import com.poulastaa.core.domain.model.PagingAlbumData
import com.poulastaa.core.domain.repository.new_album.RemoteNewAlbumDataSource
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.network.paging_source.NewAlbumPagerSource
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import javax.inject.Inject

class OnlineFirstNewAlbumDatasource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
    private val pager: NewAlbumPagerSource
) : RemoteNewAlbumDataSource {
    override fun getPagingAlbum(
        query: String,
        type: AlbumPagingType
    ): Flow<PagingData<PagingAlbumData>> {
        pager.init(query, type)

        return Pager(
            config = PagingConfig(
                pageSize = 10,
            ),
            initialKey = 1,
            pagingSourceFactory = { pager }
        ).flow
    }

    override suspend fun saveAlbums(list: List<Long>): Result<List<AlbumWithSong>, DataError.Network> {
        TODO("change existing route to except list of albumId")
    }
}
package com.poulastaa.add.network.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.Gson
import com.poulastaa.add.domain.model.DtoAddAlbum
import com.poulastaa.add.domain.model.DtoAddAlbumSearchFilterType
import com.poulastaa.add.domain.repository.RemoteAddAlbumDatasource
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.EmptyResult
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.map
import com.poulastaa.core.domain.model.AlbumId
import com.poulastaa.core.domain.model.DtoDetailedPrevSong
import com.poulastaa.core.domain.model.DtoFullAlbum
import com.poulastaa.core.domain.model.ViewType
import com.poulastaa.view.domain.repository.RemoteViewOtherDatasource
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import javax.inject.Inject

internal class OkHttpAddAlbumDatasource @Inject constructor(
    private val repo: RemoteViewOtherDatasource,
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

    override suspend fun getAlbum(
        albumId: AlbumId,
    ): Result<List<DtoDetailedPrevSong>, DataError.Network> = repo.getViewAlbum(
        type = ViewType.ALBUM,
        albumId = albumId
    ).map {
        it.listOfSongs
    }

    override suspend fun saveAlbums(list: List<AlbumId>): Result<List<DtoFullAlbum>, DataError.Network> {
        TODO("Not yet implemented")
    }
}
package com.poulastaa.add.data.repository

import androidx.paging.PagingData
import androidx.paging.filter
import com.poulastaa.add.domain.model.DtoAddAlbum
import com.poulastaa.add.domain.model.DtoAddAlbumSearchFilterType
import com.poulastaa.add.domain.repository.AddAlbumRepository
import com.poulastaa.add.domain.repository.RemoteAddAlbumDatasource
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.EmptyResult
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.asEmptyDataResult
import com.poulastaa.core.domain.model.AlbumId
import com.poulastaa.core.domain.model.DtoDetailedPrevSong
import com.poulastaa.core.domain.repository.LocalAddAlbumDatasource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class OnlineFirstAddAlbumRepository @Inject constructor(
    private val remote: RemoteAddAlbumDatasource,
    private val local: LocalAddAlbumDatasource,
    private val scope: CoroutineScope,
) : AddAlbumRepository {
    override suspend fun loadAlbum(
        query: String,
        filterType: DtoAddAlbumSearchFilterType,
    ): Flow<PagingData<DtoAddAlbum>> {
        val savedAlbumsDef = scope.async { local.loadAllSavedAlbums() }
        val pagingDef = scope.async { remote.loadAlbum(query, filterType) }

        val savedAlbums = savedAlbumsDef.await()
        val paging = pagingDef.await()

        return paging.map { list ->
            list.filter { it.id !in savedAlbums }
        }
    }

    override suspend fun getAlbum(
        albumId: AlbumId,
    ): Result<List<DtoDetailedPrevSong>, DataError.Network> = remote.getAlbum(albumId)

    override suspend fun saveAlbums(
        list: List<AlbumId>,
    ): EmptyResult<DataError.Network> = remote.saveAlbums(list).let {
        if (it is Result.Success) scope.launch { local.saveAlbums(it.data) }
        it.asEmptyDataResult()
    }
}
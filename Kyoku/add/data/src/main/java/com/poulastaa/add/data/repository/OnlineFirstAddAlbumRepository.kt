package com.poulastaa.add.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.poulastaa.add.domain.model.DtoAddAlbum
import com.poulastaa.add.domain.model.DtoAddAlbumSearchFilterType
import com.poulastaa.add.domain.repository.AddAlbumRepository
import com.poulastaa.add.domain.repository.RemoteAddAlbumDatasource
import com.poulastaa.core.domain.model.AlbumId
import com.poulastaa.core.domain.model.DtoDetailedPrevSong
import com.poulastaa.core.domain.repository.LocalAddAlbumDatasource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
        val savedAlbums = local.loadAllSavedAlbums()
        val paging = remote.loadAlbum(query, filterType)

        return paging.map { list ->
            list.map {
                if (it.id in savedAlbums) it.copy(
                    isSelected = true
                ) else it
            }
        }
    }

    override suspend fun getAlbum(albumId: AlbumId): List<DtoDetailedPrevSong> {
        TODO("Not yet implemented")
    }
}
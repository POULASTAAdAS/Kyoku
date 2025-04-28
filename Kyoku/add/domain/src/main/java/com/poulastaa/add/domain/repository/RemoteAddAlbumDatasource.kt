package com.poulastaa.add.domain.repository

import androidx.paging.PagingData
import com.poulastaa.add.domain.model.DtoAddAlbum
import com.poulastaa.add.domain.model.DtoAddAlbumSearchFilterType
import kotlinx.coroutines.flow.Flow

interface RemoteAddAlbumDatasource {
    suspend fun loadAlbum(
        query: String,
        filterType: DtoAddAlbumSearchFilterType,
    ): Flow<PagingData<DtoAddAlbum>>
}
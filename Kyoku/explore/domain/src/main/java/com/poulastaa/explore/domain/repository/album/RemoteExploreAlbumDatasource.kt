package com.poulastaa.explore.domain.repository.album

import androidx.paging.PagingData
import com.poulastaa.explore.domain.model.DtoExploreAlbumFilterType
import com.poulastaa.explore.domain.model.DtoExploreItem
import kotlinx.coroutines.flow.Flow

interface RemoteExploreAlbumDatasource {
    suspend fun getAlbum(
        query: String,
        filterType: DtoExploreAlbumFilterType,
    ): Flow<PagingData<DtoExploreItem>>
}
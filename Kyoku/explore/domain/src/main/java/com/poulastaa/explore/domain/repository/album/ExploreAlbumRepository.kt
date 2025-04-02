package com.poulastaa.explore.domain.repository.album

import androidx.paging.PagingData
import com.poulastaa.explore.domain.model.DtoExploreItem
import com.poulastaa.explore.domain.model.DtoExploreAlbumFilterType
import kotlinx.coroutines.flow.Flow

interface ExploreAlbumRepository {
    suspend fun getAlbum(
        query: String,
        filterType: DtoExploreAlbumFilterType,
    ): Flow<PagingData<DtoExploreItem>>
}
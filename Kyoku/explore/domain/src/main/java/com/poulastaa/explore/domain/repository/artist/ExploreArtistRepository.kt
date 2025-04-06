package com.poulastaa.explore.domain.repository.artist

import androidx.paging.PagingData
import com.poulastaa.explore.domain.model.DtoExploreArtistFilterType
import com.poulastaa.explore.domain.model.DtoExploreItem
import kotlinx.coroutines.flow.Flow

interface ExploreArtistRepository {
    fun getArtist(
        query: String,
        filterType: DtoExploreArtistFilterType,
    ): Flow<PagingData<DtoExploreItem>>
}
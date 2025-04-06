package com.poulastaa.explore.data.repository

import androidx.paging.PagingData
import com.poulastaa.explore.domain.model.DtoExploreArtistFilterType
import com.poulastaa.explore.domain.model.DtoExploreItem
import com.poulastaa.explore.domain.repository.artist.ExploreArtistRepository
import com.poulastaa.explore.domain.repository.artist.RemoteExploreArtistDatasource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class OnlineFirstExploreArtistRepository @Inject constructor(
    private val remote: RemoteExploreArtistDatasource,
) : ExploreArtistRepository {
    override fun getArtist(
        query: String,
        filterType: DtoExploreArtistFilterType,
    ): Flow<PagingData<DtoExploreItem>> = remote.getArtist(query, filterType)
}
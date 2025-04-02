package com.poulastaa.explore.data.repository

import androidx.paging.PagingData
import com.poulastaa.core.domain.repository.LocalExploreAlbumDatasource
import com.poulastaa.explore.domain.model.DtoExploreAlbumFilterType
import com.poulastaa.explore.domain.model.DtoExploreItem
import com.poulastaa.explore.domain.repository.album.ExploreAlbumRepository
import com.poulastaa.explore.domain.repository.album.RemoteExploreAlbumDatasource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class OnlineFirstExploreAlbumRepository @Inject constructor(
    private val remote: RemoteExploreAlbumDatasource,
    private val local: LocalExploreAlbumDatasource,
) : ExploreAlbumRepository {
    override suspend fun getAlbum(
        query: String,
        filterType: DtoExploreAlbumFilterType,
    ): Flow<PagingData<DtoExploreItem>> = remote.getAlbum(query, filterType)
}
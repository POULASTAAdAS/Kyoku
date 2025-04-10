package com.poulastaa.add.data.repository

import androidx.paging.PagingData
import com.poulastaa.add.domain.model.DtoAddSongToPlaylistPageItem
import com.poulastaa.add.domain.model.DtoAddSongToPlaylistSearchUiFilterType
import com.poulastaa.add.domain.model.DtoAddSongToPlaylistItem
import com.poulastaa.add.domain.repository.AddSongToPlaylistRepository
import com.poulastaa.add.domain.repository.RemoteAddSongToPlaylistDatasource
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class OnlineFirstAddSongToPlaylistRepository @Inject constructor(
    private val remote: RemoteAddSongToPlaylistDatasource,
) : AddSongToPlaylistRepository {
    override suspend fun loadStaticData(): Result<List<DtoAddSongToPlaylistPageItem>, DataError.Network> =
        remote.loadStaticData()

    override fun search(
        query: String,
        filterType: DtoAddSongToPlaylistSearchUiFilterType,
    ): Flow<PagingData<DtoAddSongToPlaylistItem>> = remote.search(query, filterType)
}
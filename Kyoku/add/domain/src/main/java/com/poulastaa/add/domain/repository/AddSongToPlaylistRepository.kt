package com.poulastaa.add.domain.repository

import androidx.paging.PagingData
import com.poulastaa.add.domain.model.DtoAddSongToPlaylistPageItem
import com.poulastaa.add.domain.model.DtoAddSongToPlaylistSearchUiFilterType
import com.poulastaa.add.domain.model.DtoAddSongToPlaylistItem
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import kotlinx.coroutines.flow.Flow

interface AddSongToPlaylistRepository {
    suspend fun loadStaticData(): Result<List<DtoAddSongToPlaylistPageItem>, DataError.Network>
    fun search(
        query: String,
        filterType: DtoAddSongToPlaylistSearchUiFilterType,
    ): Flow<PagingData<DtoAddSongToPlaylistItem>>
}
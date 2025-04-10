package com.poulastaa.add.domain.repository

import com.poulastaa.add.domain.model.DtoAddSongToPlaylistPageItem
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result

interface AddSongToPlaylistRepository {
    suspend fun loadStaticData(): Result<List<DtoAddSongToPlaylistPageItem>, DataError.Network>
}
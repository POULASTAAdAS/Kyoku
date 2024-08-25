package com.poulastaa.core.domain.repository.add_playlist

import com.poulastaa.core.domain.AddPlaylistResType
import com.poulastaa.core.domain.ExploreType
import com.poulastaa.core.domain.model.PrevSong
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result

interface AddPlaylistRepository {
    suspend fun getSongId(type: ExploreType): List<PrevSong>
    suspend fun savePlaylist(
        idList: List<Long>,
        name: String,
        type: ExploreType,
    ): Result<AddPlaylistResType, DataError.Network>
}
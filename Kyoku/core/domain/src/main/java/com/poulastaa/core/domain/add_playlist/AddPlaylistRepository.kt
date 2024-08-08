package com.poulastaa.core.domain.add_playlist

import com.poulastaa.core.domain.AddPlaylistResType
import com.poulastaa.core.domain.ExploreType
import com.poulastaa.core.domain.model.PlaylistData
import com.poulastaa.core.domain.model.PrevSong
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import javax.xml.crypto.Data

interface AddPlaylistRepository {
    suspend fun getSongId(type: ExploreType): List<PrevSong>
    suspend fun savePlaylist(
        idList: List<Long>,
        name: String,
        type: ExploreType,
    ): Result<AddPlaylistResType, DataError.Network>
}
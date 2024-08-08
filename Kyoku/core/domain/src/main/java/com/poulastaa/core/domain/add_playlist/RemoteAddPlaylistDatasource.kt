package com.poulastaa.core.domain.add_playlist

import com.poulastaa.core.domain.ExploreType
import com.poulastaa.core.domain.model.PlaylistData
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result

interface RemoteAddPlaylistDatasource {
    suspend fun savePlaylist(
        idList: List<Long>,
        name: String,
        type: ExploreType,
    ): Result<PlaylistData, DataError.Network>
}
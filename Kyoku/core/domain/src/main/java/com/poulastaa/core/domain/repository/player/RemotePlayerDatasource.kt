package com.poulastaa.core.domain.repository.player

import com.poulastaa.core.domain.model.AlbumWithSong
import com.poulastaa.core.domain.model.PlaylistData
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result

interface RemotePlayerDatasource {
    suspend fun getAlbum(id: Long): Result<AlbumWithSong, DataError.Network>
    suspend fun getPlaylist(id: Long): Result<PlaylistData, DataError.Network>
}
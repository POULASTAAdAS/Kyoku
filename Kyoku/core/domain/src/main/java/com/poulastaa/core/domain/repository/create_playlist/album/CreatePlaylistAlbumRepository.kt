package com.poulastaa.core.domain.repository.create_playlist.album

import com.poulastaa.core.domain.model.AlbumWithSong
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result

interface CreatePlaylistAlbumRepository {
    suspend fun getAlbum(
        albumId: Long,
        savedSongIdList: List<Long>
    ): Result<AlbumWithSong, DataError.Network>
}
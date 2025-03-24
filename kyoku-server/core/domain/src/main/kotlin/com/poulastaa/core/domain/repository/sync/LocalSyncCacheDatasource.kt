package com.poulastaa.core.domain.repository.sync

import com.poulastaa.core.domain.model.DtoSong
import com.poulastaa.core.domain.repository.AlbumId
import com.poulastaa.core.domain.repository.SongId

interface LocalSyncCacheDatasource {
    suspend fun cacheSongById(list: List<SongId>): List<DtoSong>
    suspend fun cacheSavedAlbumIdList(userId: Long): List<AlbumId>

}
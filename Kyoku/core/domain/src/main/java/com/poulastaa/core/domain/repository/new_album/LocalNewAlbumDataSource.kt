package com.poulastaa.core.domain.repository.new_album

import com.poulastaa.core.domain.model.AlbumWithSong

interface LocalNewAlbumDataSource {
    suspend fun getNotSavedAlbumIdList(list: List<Long>): List<Long>
    suspend fun saveAlbums(list: List<AlbumWithSong>)
}
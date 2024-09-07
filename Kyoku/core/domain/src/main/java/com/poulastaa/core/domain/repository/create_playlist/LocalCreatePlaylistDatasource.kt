package com.poulastaa.core.domain.repository.create_playlist

import com.poulastaa.core.domain.model.Song

interface LocalCreatePlaylistDatasource {
    suspend fun saveSong(song: Song, playlistId: Long)
}
package com.poulastaa.core.domain.repository.create_playlist.artist

import com.poulastaa.core.domain.model.Artist

interface LocalCreatePlaylistArtistDatasource {
    suspend fun getArtist(artistId: Long): Artist?
}
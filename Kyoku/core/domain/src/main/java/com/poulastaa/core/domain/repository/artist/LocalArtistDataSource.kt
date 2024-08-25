package com.poulastaa.core.domain.repository.artist

import com.poulastaa.core.domain.model.Artist

interface LocalArtistDataSource {
    suspend fun insertArtist(artist: List<Artist>)
}
package com.poulastaa.core.domain.artist

import com.poulastaa.core.domain.model.Artist

interface LocalArtistDataSource {
    suspend fun insertArtist(artist: List<Artist>)
}
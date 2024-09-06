package com.poulastaa.core.domain.repository.new_artist

import com.poulastaa.core.domain.model.Artist

interface LocalNewArtistDataSource {
    suspend fun saveArtist(list: List<Artist>)
}
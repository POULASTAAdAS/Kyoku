package com.poulastaa.core.domain.view_artist

import com.poulastaa.core.domain.model.Artist

interface LocalViewArtistDatasource {
    suspend fun getArtist(artistId: Long): Artist?

    suspend fun saveArtist(entry: Artist)

    suspend fun followArtist(artistId: Long)
    suspend fun onFollowArtist(artistId: Long)
}
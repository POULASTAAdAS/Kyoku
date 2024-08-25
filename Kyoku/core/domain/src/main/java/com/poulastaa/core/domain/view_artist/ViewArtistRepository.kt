package com.poulastaa.core.domain.view_artist

import com.poulastaa.core.domain.model.Artist

interface ViewArtistRepository {
    suspend fun getArtist(artistId: String): Artist

    suspend fun followArtist(artistId: Long)
    suspend fun onFollowArtist(artistId: Long)
}
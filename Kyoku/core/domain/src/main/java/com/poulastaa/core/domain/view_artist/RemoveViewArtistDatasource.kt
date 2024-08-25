package com.poulastaa.core.domain.view_artist

import com.poulastaa.core.domain.model.Artist

interface RemoveViewArtistDatasource {
    suspend fun getArtistOnId(artistId: Long): Artist

    suspend fun followArtist(artistId: Long)
    suspend fun unFollowArtist(artistId: Long)
}
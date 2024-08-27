package com.poulastaa.core.domain.repository.explore_artist

import com.poulastaa.core.domain.model.Artist

interface LocalExploreArtistDatasource {
    suspend fun getArtist(artistId: Long): Artist?

//    suspend fun saveAlbum()

    suspend fun followArtist(artist: Artist)
    suspend fun unFollowArtist(artistId: Long)
}
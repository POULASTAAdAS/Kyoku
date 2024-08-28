package com.poulastaa.core.domain.repository.view_artist

import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.domain.model.Song

interface LocalViewArtistDatasource {
    suspend fun getArtist(artistId: Long): Artist?

    suspend fun isSongInFavourite(songId: Long): Boolean

    suspend fun followArtist(artist: Artist)
    suspend fun unFollowArtist(artistId: Long)

    suspend fun addSongToFavourite(song: Song)
}
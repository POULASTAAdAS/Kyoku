package com.poulastaa.core.domain.repository.explore_artist

import com.poulastaa.core.domain.model.AlbumWithSong
import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.domain.model.Song

interface LocalExploreArtistDatasource {
    suspend fun getArtist(artistId: Long): Artist?

    suspend fun followArtist(artist: Artist)
    suspend fun unFollowArtist(artistId: Long)

    suspend fun isSongInFavourite(songId: Long): Boolean
    suspend fun isAlbumSaved(albumId: Long): Boolean

    suspend fun saveAlbum(data: AlbumWithSong)
    suspend fun addSongToFavourite(song: Song)
}
package com.poulastaa.core.domain.home

import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.domain.model.DayType
import com.poulastaa.core.domain.model.HomeData
import com.poulastaa.core.domain.model.NewHome
import com.poulastaa.core.domain.model.Song
import com.poulastaa.core.domain.utils.SavedPlaylist
import kotlinx.coroutines.flow.Flow


interface LocalHomeDatasource {
    suspend fun storeNewHomeResponse(
        dayType: DayType,
        response: NewHome,
    )

    suspend fun isNewUser(): Boolean

    suspend fun loadHomeStaticData(): HomeData
    fun loadSavedPlaylist(): Flow<SavedPlaylist>

    suspend fun isArtistIsInLibrary(artistId: Long): Boolean
    suspend fun isAlbumInLibrary(albumId: Long): Boolean
    suspend fun isSongInFavourite(songId: Long): Boolean

    suspend fun isSongInDatabase(id: Long): Boolean

    suspend fun addSong(song: Song)

    suspend fun insertIntoFavourite(id: Long)
    suspend fun removeSongFromFavourite(id: Long)

    suspend fun followArtist(artist: Artist)
    suspend fun unFollowArtist(id: Long)
}
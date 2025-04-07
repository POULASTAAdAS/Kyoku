package com.poulastaa.core.database.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
internal interface ProfileDao {
    @Query("SELECT COUNT(*) FROM EntityPlaylist")
    suspend fun countSavedPlaylist(): Int

    @Query("SELECT COUNT(*) FROM EntitySavedArtist")
    suspend fun countSavedArtist(): Int

    @Query("SELECT COUNT(*) FROM EntitySavedAlbum")
    suspend fun countSavedAlbum(): Int

    @Query("SELECT COUNT(*) FROM EntityFavourite")
    suspend fun countFavouriteSongs(): Int
}
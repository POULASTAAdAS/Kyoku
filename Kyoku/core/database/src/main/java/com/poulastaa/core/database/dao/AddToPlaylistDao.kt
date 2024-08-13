package com.poulastaa.core.database.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface AddToPlaylistDao {
    @Query("select id from FavouriteEntity where id = :songId")
    suspend fun getFavouriteEntryOnSongId(songId: Long): Long?

    @Query("select count(*) from FavouriteEntity")
    suspend fun getTotalFavouriteEntryCount(): Int
}
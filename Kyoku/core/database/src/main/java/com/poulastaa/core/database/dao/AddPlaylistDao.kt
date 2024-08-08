package com.poulastaa.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.poulastaa.core.database.entity.PlaylistEntity
import com.poulastaa.core.domain.model.PrevSong

@Dao
interface AddPlaylistDao {
    @Query("select id as songId , coverImage from PopularSongFromYourTimePrevEntity")
    suspend fun getOldGem(): List<PrevSong>

    @Query("select id as songId , coverImage from FavouriteArtistMixPrevEntity")
    suspend fun getFavouriteArtistMix(): List<PrevSong>

    @Query("select id as songId , coverImage from PopularSongMixPrevEntity")
    suspend fun getPopularSongMix(): List<PrevSong>

    @Query("select * from PlaylistEntity where name = :name")
    suspend fun isNameConflict(name: String): List<PlaylistEntity>
}
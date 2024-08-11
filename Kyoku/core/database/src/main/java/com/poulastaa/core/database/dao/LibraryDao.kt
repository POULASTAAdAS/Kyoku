package com.poulastaa.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.poulastaa.core.database.entity.ArtistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LibraryDao {
    @Query("select * from ArtistEntity")
    fun getAllSavedArtist(): Flow<List<ArtistEntity>>
}
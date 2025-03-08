package com.poulastaa.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.poulastaa.core.domain.model.ArtistId

@Dao
internal interface ViewDao {
    @Query("SELECT EXISTS(SELECT 1 FROM EntityArtist WHERE id = :artistId)")
    suspend fun isArtistFollowed(artistId: ArtistId): Boolean
}
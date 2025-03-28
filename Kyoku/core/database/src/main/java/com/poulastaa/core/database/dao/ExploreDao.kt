package com.poulastaa.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.poulastaa.core.database.entity.EntityArtist
import com.poulastaa.core.database.entity.EntityPrevArtist
import com.poulastaa.core.domain.model.ArtistId

@Dao
interface ExploreDao {
    @Query("Select * from EntityArtist where id = :artistId")
    suspend fun getArtistOnId(artistId: ArtistId): EntityArtist?

    @Query("Select * from EntityPrevArtist where id = :artistId")
    suspend fun getPrevArtistOnId(artistId: ArtistId): EntityPrevArtist?
}
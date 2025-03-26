package com.poulastaa.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.poulastaa.core.database.relation.SuggestedArtistWithSong
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.SongId

@Dao
interface RefreshDao {
    @Query("SELECT EntityPrevExplore.dataId FROM EntityPrevExplore where typeId = :typeId")
    suspend fun getExploreTypeIdList(typeId: Int): List<SongId>

    @Query("SELECT EntityPrevArtist.id FROM EntityPrevArtist")
    suspend fun getSavedSuggestedArtist(): List<ArtistId>

    @Query("SELECT EntityPrevAlbum.id FROM EntityPrevAlbum")
    suspend fun getSavedSuggestedAlbum(): List<ArtistId>

    @Transaction
    @Query("Select * from EntityPrevArtist")
    suspend fun getSuggestedArtistSong(): List<SuggestedArtistWithSong>

    @Query("DELETE FROM EntityPrevExplore")
    suspend fun clearPrevExploreType()

    @Query("DELETE FROM EntityPrevArtist")
    suspend fun clearSuggestedArtist()

    @Query("DELETE FROM EntityPrevAlbum")
    suspend fun clearSuggestedAlbum()

    @Query("DELETE FROM EntityRelationSuggestedSongByArtist")
    suspend fun clearSuggestedArtistSong()
}
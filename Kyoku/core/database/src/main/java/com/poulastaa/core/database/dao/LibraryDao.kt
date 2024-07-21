package com.poulastaa.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.poulastaa.core.database.entity.ArtistEntity
import com.poulastaa.core.database.model.PrevSongResult
import kotlinx.coroutines.flow.Flow

@Dao
interface LibraryDao {
    @Query(
        """
        select PlaylistEntity.id , PlaylistEntity.name ,  SongEntity.coverImage from SongEntity
        join SongPlaylistRelationEntity on SongEntity.id = SongPlaylistRelationEntity.songId
        join PlaylistEntity on PlaylistEntity.id = SongPlaylistRelationEntity.playlistId
        where PlaylistEntity.id order by PlaylistEntity.points
    """
    )
    fun getAllSavedPlaylist(): Flow<List<PrevSongResult>>

    @Query("select * from ArtistEntity")
    fun getAllSavedArtist(): Flow<List<ArtistEntity>>
}
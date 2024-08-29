package com.poulastaa.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.poulastaa.core.database.model.PrevSongResult
import kotlinx.coroutines.flow.Flow

@Dao
interface AddToPlaylistDao {
    @Query("select id from FavouriteEntity where id = :songId")
    suspend fun getFavouriteEntryOnSongId(songId: Long): Long?

    @Query("select count(*) from FavouriteEntity")
    suspend fun getTotalFavouriteEntryCount(): Int

    @Query("select songId from SongPlaylistRelationEntity where songId = :songId and playlistId = :playlistId")
    suspend fun getSongPlaylistRelationEntryOnSongId(songId: Long, playlistId: Long): Long?

    @Query("select id from SongEntity where id = :songId")
    suspend fun checkIfSongInDatabase(songId: Long): Long?

    @Query(
        """
        SELECT PlaylistEntity.id, PlaylistEntity.name, SongEntity.coverImage
        FROM PlaylistEntity
        LEFT JOIN SongPlaylistRelationEntity ON PlaylistEntity.id = SongPlaylistRelationEntity.playlistId
        LEFT JOIN SongEntity ON SongEntity.id = SongPlaylistRelationEntity.songId
        ORDER BY PlaylistEntity.id DESC
    """
    )
    fun getAllSavedPlaylist(): Flow<List<PrevSongResult>>
}
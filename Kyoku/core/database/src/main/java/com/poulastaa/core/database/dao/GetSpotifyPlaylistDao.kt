package com.poulastaa.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.poulastaa.core.database.model.PlaylistResult
import kotlinx.coroutines.flow.Flow

@Dao
interface GetSpotifyPlaylistDao {
    @Query(
        """
        select SongEntity.id as songId , SongEntity.coverImage as songCoverImage,
        SongEntity.title as songTitle, SongEntity.artistName as artist, PlaylistEntity.id as playlistId,
        PlaylistEntity.name as playlistName from SongEntity
        join SongPlaylistRelationEntity on SongPlaylistRelationEntity.songId = SongEntity.id
        join PlaylistEntity on PlaylistEntity.id = SongPlaylistRelationEntity.playlistId
        where PlaylistEntity.id order by PlaylistEntity.id desc
    """
    )
    fun getAllPlaylist(): Flow<List<PlaylistResult>>
}
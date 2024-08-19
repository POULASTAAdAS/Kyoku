package com.poulastaa.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.poulastaa.core.database.model.PlaylistResult

@Dao
interface ViewDao {
    @Query(
        """
         select PlaylistEntity.id as playlistId, PlaylistEntity.name as playlistName, 
        SongEntity.id as songId, SongEntity.title as songTitle, SongEntity.artistName as artist,
        SongEntity.coverImage as songCoverImage from PlaylistEntity
        join SongPlaylistRelationEntity on SongPlaylistRelationEntity.playlistId = PlaylistEntity.id
        join SongEntity on SongEntity.id = SongPlaylistRelationEntity.songId
        where PlaylistEntity.id = :id
    """
    )
    suspend fun getPlaylistOnId(id: Long): List<PlaylistResult>

    @Query(
        """
            select AlbumEntity.id as playlistId, AlbumEntity.name as playlistName, 
            SongEntity.id as songId, SongEntity.title as songTitle, SongEntity.artistName as artist,
            SongEntity.coverImage as songCoverImage  from AlbumEntity
            join SongAlbumRelationEntity on SongAlbumRelationEntity.albumId = AlbumEntity.id
            join SongEntity on SongEntity.id = SongAlbumRelationEntity.songId
            where AlbumEntity.id = :id
        """
    )
    suspend fun getAlbumOnId(id: Long): List<PlaylistResult>
}
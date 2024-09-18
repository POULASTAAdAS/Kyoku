package com.poulastaa.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import com.poulastaa.core.database.entity.FavouriteEntity
import com.poulastaa.core.database.entity.PlaylistEntity

@Dao
interface WorkDao {
    @Query("select id from AlbumEntity")
    suspend fun getAllSavedAlbumId(): List<Long>

    @Query("select id from PlaylistEntity")
    suspend fun getAllSavedPlaylistId(): List<Long>

    @Query("select id from ArtistEntity")
    suspend fun getAllSavedArtistId(): List<Long>

    @Query("delete from AlbumEntity where id in (:list)")
    suspend fun deleteAlbums(list: List<Long>)

    @Query("delete from PlaylistEntity where id in (:list)")
    suspend fun deletePlaylist(list: List<Long>)

    @Query("delete from ArtistEntity where id in (:list)")
    suspend fun deleteArtists(list: List<Long>)

    @Delete
    suspend fun deleteFavourites(list: List<FavouriteEntity>)

    @Update
    suspend fun updatePlaylist(entry: PlaylistEntity)

    @Query("delete from SongPlaylistRelationEntity where playlistId = :playlistId and songId in (:songIdList)")
    suspend fun deletePlaylistSongs(playlistId: Long, songIdList: List<Long>)
}
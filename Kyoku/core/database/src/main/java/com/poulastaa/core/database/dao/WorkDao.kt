package com.poulastaa.core.database.dao

import androidx.room.Dao
import androidx.room.Query

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
}
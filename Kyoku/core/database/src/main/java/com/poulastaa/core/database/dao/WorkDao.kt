package com.poulastaa.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.poulastaa.core.database.entity.EntityRelationSongPlaylist
import com.poulastaa.core.database.entity.EntitySavedAlbum
import com.poulastaa.core.database.entity.EntitySavedArtist
import com.poulastaa.core.domain.model.AlbumId
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.PlaylistId
import com.poulastaa.core.domain.model.SongId

@Dao
internal interface WorkDao {
    @Query("Select * from EntitySavedAlbum")
    suspend fun getAllSavedAlbumIds(): List<AlbumId>

    @Query("Select EntityPlaylist.id from EntityPlaylist")
    suspend fun getAllSavedPlaylistIds(): List<PlaylistId>

    @Query("Select * from EntitySavedArtist")
    suspend fun getAllSavedArtistIds(): List<ArtistId>

    @Query("Select * from EntityFavourite")
    suspend fun getAllSavedFavouriteSongIds(): List<SongId>

    @Delete
    suspend fun removeSavedAlbums(list: List<EntitySavedAlbum>)

    @Query("Delete from EntityPlaylist where id in (:list)")
    suspend fun removeSavedPlaylists(list: List<PlaylistId>)

    @Delete
    suspend fun removeSavedArtists(list: List<EntitySavedArtist>)

    @Query("Delete from EntityFavourite where songId in (:list)")
    suspend fun removeSavedFavouriteSongs(list: List<SongId>)

    @Query("Select * from EntityRelationSongPlaylist")
    suspend fun getAllSavedPlaylistSongs(): List<EntityRelationSongPlaylist>

    @Delete
    suspend fun removeSavedPlaylistSongs(list: List<EntityRelationSongPlaylist>)
}
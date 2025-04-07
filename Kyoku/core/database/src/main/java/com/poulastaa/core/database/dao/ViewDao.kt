package com.poulastaa.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.poulastaa.core.database.entity.EntityAlbum
import com.poulastaa.core.database.entity.EntityArtist
import com.poulastaa.core.database.entity.EntityExplore
import com.poulastaa.core.database.entity.EntityPlaylist
import com.poulastaa.core.domain.model.AlbumId
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.DtoDetailedPrevSong
import com.poulastaa.core.domain.model.PlaylistId
import com.poulastaa.core.domain.model.SongId

@Dao
internal interface ViewDao {
    @Query("SELECT EXISTS(SELECT 1 FROM EntitySavedArtist WHERE id = :artistId)")
    suspend fun isArtistFollowed(artistId: ArtistId): Boolean

    @Query("SELECT dataId FROM EntityExplore where typeId = :type")
    suspend fun getExploreTypePrevData(type: Int): List<SongId>

    @Query("SELECT * FROM EntityPlaylist WHERE id = :playlistId")
    suspend fun getPlaylistOnId(playlistId: PlaylistId): EntityPlaylist?

    @Query(
        """
        SELECT EntitySong.id, EntitySong.title, EntitySong.poster, EntitySongInfo.composer as artists, EntitySongInfo.releaseYear FROM EntitySong
        JOIN EntitySongInfo on EntitySong.id = EntitySongInfo.songId
        JOIN EntityRelationSongPlaylist on EntitySong.id = EntityRelationSongPlaylist.songId
        where EntityRelationSongPlaylist.playlistId = :playlistId
    """
    )
    suspend fun getPlaylistPrevSongs(playlistId: PlaylistId): List<DtoDetailedPrevSong>

    @Query("SELECT * FROM EntityAlbum WHERE id = :albumId")
    suspend fun getAlbumOnId(albumId: Long): EntityAlbum?

    @Query(
        """
        SELECT EntitySong.id, EntitySong.title, EntitySong.poster, EntitySongInfo.composer as artists, EntitySongInfo.releaseYear FROM EntitySong
        JOIN EntitySongInfo on EntitySong.id = EntitySongInfo.songId
        JOIN EntityFavourite on EntitySong.id = EntityFavourite.songId
    """
    )
    suspend fun getFavoriteSongs(): List<DtoDetailedPrevSong>

    @Query(
        """
        SELECT EntitySong.id, EntitySong.title, EntitySong.poster, EntitySongInfo.composer as artists, EntitySongInfo.releaseYear FROM EntitySong
        JOIN EntitySongInfo on EntitySong.id = EntitySongInfo.songId
        JOIN EntityRelationSongAlbum on EntitySong.id = EntityRelationSongAlbum.songId
        where EntityRelationSongAlbum.albumId = :albumId
    """
    )
    suspend fun getAlbumPrevSongs(albumId: AlbumId): List<DtoDetailedPrevSong>

    @Query(
        """
        SELECT EntitySong.id, EntitySong.title, EntitySong.poster, EntitySongInfo.composer as artists, EntitySongInfo.releaseYear FROM EntitySong
        JOIN EntitySongInfo on EntitySong.id = EntitySongInfo.songId
        JOIN EntityExplore on EntitySong.id = EntityExplore.dataId
        where EntityExplore.typeId = :typeId
    """
    )
    suspend fun getExploreTypeSongs(typeId: Int): List<DtoDetailedPrevSong>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExploreTypeSong(entrys: List<EntityExplore>)

    @Query("Select * from EntityArtist where id in (Select * from EntitySavedArtist)")
    suspend fun getSavedArtist(): List<EntityArtist>

    @Query("Select * from EntityAlbum where id in (Select * from EntitySavedAlbum)")
    suspend fun getSavedAlbums(): List<EntityAlbum>

    @Query(
        """
        select EntityArtist.coverImage from EntityArtist
        join EntityRelationArtistAlbum on EntityRelationArtistAlbum.artistId = EntityArtist.id
        join EntityAlbum on EntityAlbum.id = EntityRelationArtistAlbum.albumId
        where EntityAlbum.id = :albumId
    """
    )
    suspend fun getArtistOnAlbumId(albumId: AlbumId): List<String>

    @Query("Select * from EntityPlaylist")
    suspend fun getSavedPlaylist(): List<EntityPlaylist>

    @Query(
        """
        select EntitySong.poster  from EntitySong
        join EntityRelationSongPlaylist on EntityRelationSongPlaylist.songId = EntitySong.id
        where EntityRelationSongPlaylist.playlistId = :playlistId
    """
    )
    suspend fun getSavedPlaylistCover(playlistId: PlaylistId): List<String>

    @Query(
        """
        select count(*)  from EntitySong
        join EntityRelationSongPlaylist on EntityRelationSongPlaylist.songId = EntitySong.id
        where EntityRelationSongPlaylist.playlistId = :playlistId
    """
    )
    suspend fun countPlaylistSongs(playlistId: PlaylistId): Int
}
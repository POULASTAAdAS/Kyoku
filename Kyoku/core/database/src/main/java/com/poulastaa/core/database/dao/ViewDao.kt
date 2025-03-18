package com.poulastaa.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.poulastaa.core.database.entity.EntityAlbum
import com.poulastaa.core.database.entity.EntityExplore
import com.poulastaa.core.database.entity.EntityPlaylist
import com.poulastaa.core.domain.model.AlbumId
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.DtoDetailedPrevSong
import com.poulastaa.core.domain.model.PlaylistId
import com.poulastaa.core.domain.model.SongId

@Dao
internal interface ViewDao {
    @Query("SELECT EXISTS(SELECT 1 FROM EntityArtist WHERE id = :artistId)")
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
}
package com.poulastaa.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.poulastaa.core.database.entity.AlbumEntity
import com.poulastaa.core.database.entity.SongEntity
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
    suspend fun getAlbumForViewData(id: Long): List<PlaylistResult>

    @Query("select * from AlbumEntity where id = :id")
    suspend fun getAlbumOnId(id: Long): AlbumEntity?

    @Query("select id from FavouriteArtistMixEntity")
    suspend fun getFevArtistMixSongIds(): List<Long>

    @Query("select id from PopularSongMixEntity")
    suspend fun getPopularSongMixSongIds(): List<Long>


    @Query("select id from PopularSongFromYourTimeEntity")
    suspend fun getOldMixSongIds(): List<Long>


    @Query("select id from DayTypeSongEntity")
    suspend fun getDayTypeMixSongIds(): List<Long>



    @Query("select * from SongEntity where id in (:list)")
    suspend fun getSongOnIdList(list: List<Long>): List<SongEntity>

    @Query("select id from FavouriteArtistMixPrevEntity")
    suspend fun getPrevFevArtistMixSongIds(): List<Long>

    @Query("select id from PopularSongMixPrevEntity")
    suspend fun getPrevPopularSongMixSongIds(): List<Long>


    @Query("select id from PopularSongFromYourTimePrevEntity")
    suspend fun getPrevOldMixSongIds(): List<Long>


    @Query("select id from DayTypeSongPrevEntity")
    suspend fun getPrevDayTypeMixSongIds(): List<Long>
}
package com.poulastaa.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.poulastaa.core.database.entity.DayTypeSongPrevEntity
import com.poulastaa.core.database.entity.FavouriteArtistMixPrevEntity
import com.poulastaa.core.database.entity.PopularAlbumPrevEntity
import com.poulastaa.core.database.entity.PopularSongFromYourTimePrevEntity
import com.poulastaa.core.database.entity.PopularSongMixPrevEntity
import com.poulastaa.core.database.entity.PopularSuggestArtistEntity
import com.poulastaa.core.database.entity.popular_artist_song.ArtistSongEntity
import com.poulastaa.core.database.entity.popular_artist_song.PopularSongArtistEntity
import com.poulastaa.core.database.entity.relation.PopularArtistSongRelationEntity
import com.poulastaa.core.database.model.PopularArtistWithSongResult

@Dao
interface HomeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPopularSongMixPrevs(entrys: List<PopularSongMixPrevEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPopularSongFromYourTimePrevs(entrys: List<PopularSongFromYourTimePrevEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavouriteArtistMixPrevs(entrys: List<FavouriteArtistMixPrevEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDayTypeSongPrevs(entrys: List<DayTypeSongPrevEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPopularAlbumPrevs(entrys: List<PopularAlbumPrevEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPopularSuggestArtists(entrys: List<PopularSuggestArtistEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPopularSongArtist(entry: PopularSongArtistEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArtistSongs(entrys: List<ArtistSongEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPopularArtistSongRelations(entrys: List<PopularArtistSongRelationEntity>)

    @Query("select id from FavouriteArtistMixPrevEntity")
    suspend fun isNewUser(): List<Long>

    @Query("select * from PopularSongMixPrevEntity")
    suspend fun readPopularSongMixPrev(): List<PopularSongMixPrevEntity>

    @Query("select * from PopularSongFromYourTimePrevEntity")
    suspend fun readPopularSongFromYourTimePrev(): List<PopularSongFromYourTimePrevEntity>

    @Query("select * from FavouriteArtistMixPrevEntity")
    suspend fun readFavouriteArtistMixPrev(): List<FavouriteArtistMixPrevEntity>

    @Query("select * from DayTypeSongPrevEntity")
    suspend fun readDayTypeSong(): List<DayTypeSongPrevEntity>

    @Query("select * from PopularAlbumPrevEntity")
    suspend fun readPopularAlbum(): List<PopularAlbumPrevEntity>

    @Query("select * from PopularSuggestArtistEntity")
    suspend fun readPopularSuggestArtist(): List<PopularSuggestArtistEntity>


    @Query(
        """
        select PopularSongArtistEntity.id as artistId, PopularSongArtistEntity.name , PopularSongArtistEntity.coverImage as artistCover,
        ArtistSongEntity.id as songId, ArtistSongEntity.title , ArtistSongEntity.coverImage  from PopularArtistSongRelationEntity
        join ArtistSongEntity on ArtistSongEntity.id = PopularArtistSongRelationEntity.songId
        join PopularSongArtistEntity on PopularArtistSongRelationEntity.artistId = PopularSongArtistEntity.id
        where ArtistSongEntity.id
    """
    )
    suspend fun readPopularArtistSong(): List<PopularArtistWithSongResult>

    @Query(
        """
        select id from ArtistEntity where id = :artistId
    """
    )
    suspend fun isArtistIsInLibrary(artistId: Long): Long?

    @Query("select id from AlbumEntity where id = :albumId")
    suspend fun isAlbumInLibrary(albumId: Long): Long?

    @Query("select id from FavouriteEntity where id = :songId")
    suspend fun isSongInFavourite(songId: Long): Long?

    @Query("select id from SongEntity where id = :id")
    suspend fun getSong(id: Long): Long?
}
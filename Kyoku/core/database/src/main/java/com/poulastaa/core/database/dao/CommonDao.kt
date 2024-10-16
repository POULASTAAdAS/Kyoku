package com.poulastaa.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.poulastaa.core.database.entity.AlbumEntity
import com.poulastaa.core.database.entity.ArtistEntity
import com.poulastaa.core.database.entity.DayTypeSongEntity
import com.poulastaa.core.database.entity.FavouriteArtistMixEntity
import com.poulastaa.core.database.entity.FavouriteEntity
import com.poulastaa.core.database.entity.PlaylistEntity
import com.poulastaa.core.database.entity.PopularSongFromYourTimeEntity
import com.poulastaa.core.database.entity.PopularSongMixEntity
import com.poulastaa.core.database.entity.SongEntity
import com.poulastaa.core.database.entity.relation.SongAlbumRelationEntity
import com.poulastaa.core.database.entity.relation.SongArtistRelationEntity
import com.poulastaa.core.database.entity.relation.SongPlaylistRelationEntity
import com.poulastaa.core.database.model.PrevSongResult
import com.poulastaa.core.database.model.SongColorResult
import com.poulastaa.core.domain.model.PrevAlbum
import kotlinx.coroutines.flow.Flow

@Dao
interface CommonDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSong(song: SongEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSongs(songs: List<SongEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArtist(artist: ArtistEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArtists(artists: List<ArtistEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylists(playlists: List<PlaylistEntity>)


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSongArtistRelation(songArtistRelations: SongArtistRelationEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSongArtistRelations(list: List<SongArtistRelationEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSongPlaylistRelations(list: List<SongPlaylistRelationEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSongPlaylistRelation(entry: SongPlaylistRelationEntity)

    @Delete
    suspend fun deleteSongPlaylistRelation(entry: SongPlaylistRelationEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOneIntoFavourite(entry: FavouriteEntity)

    @Delete
    suspend fun deleteSongFromFavourite(entry: FavouriteEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMultipleIntoFavourite(entrys: List<FavouriteEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlbums(entity: List<AlbumEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlbum(entity: AlbumEntity)

    @Query("select id from SongEntity where coverImage = :url")
    suspend fun getSongOnUrl(url: String): Long?

    @Query("select id from FavouriteEntity limit 1")
    suspend fun isFavourite(): List<Long>

    @Query("delete from ArtistEntity where id = :id")
    suspend fun deleteArtist(id: Long)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSongAlbumRelation(entrys: List<SongAlbumRelationEntity>)

    @Query("delete from AlbumEntity where id = :id")
    suspend fun deleteAlbum(id: Long)


    @Query(" select `primary`, background, onBackground from SongEntity  where id = :songId ")
    suspend fun getSongColorInfo(songId: Long): SongColorResult

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

    @Query("select id as albumId , name as name , coverImage as coverImage from AlbumEntity")
    fun getAllSavedAlbum(): Flow<List<PrevAlbum>>

    @Query(
        """
        update SongEntity set `primary` = :primary ,background = :background ,
        onBackground = :onBackground where id = :songId
    """
    )
    suspend fun updateCoverAndColor(
        songId: Long,
        primary: String,
        background: String,
        onBackground: String,
    )

    @Query("select * from AlbumEntity where id = :id")
    suspend fun getAlbumById(id: Long): AlbumEntity

    @Query("select * from ArtistEntity where id = :id")
    suspend fun getArtistById(id: Long): ArtistEntity

    @Query("select * from ArtistEntity where id = :id")
    suspend fun getArtistByIdOrNull(id: Long): ArtistEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIntoDayType(entrys: List<DayTypeSongEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIntoArtistMix(entrys: List<FavouriteArtistMixEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIntoOldMix(entrys: List<PopularSongFromYourTimeEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIntoPopularSongMix(entrys: List<PopularSongMixEntity>)

    @Query("select id from FavouriteEntity where id = :id")
    suspend fun isSongInFavourite(id: Long): Long?

    @Query("select id from FavouriteEntity")
    suspend fun getFevSongIds(): List<Long>

    @Query("select id from PlaylistEntity where name = :name")
    suspend fun getPlaylistByName(name: String): Long?

    @Query(
        """
        select SongEntity.* from SongEntity
        join SongPlaylistRelationEntity on SongPlaylistRelationEntity.songId = SongEntity.id
        where SongPlaylistRelationEntity.playlistId = :playlistId
    """
    )
    fun getPlaylistSongsAsFlow(playlistId: Long): Flow<List<SongEntity>>

    @Query(
        """
        select SongEntity.* from SongEntity
        join FavouriteEntity on FavouriteEntity.id = SongEntity.id
    """
    )
    fun getFavouriteSongsAsFlow(): Flow<List<SongEntity>>
}
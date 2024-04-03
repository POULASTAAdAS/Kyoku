package com.poulastaa.kyoku.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.poulastaa.kyoku.data.model.database.AlbumPrevResult
import com.poulastaa.kyoku.data.model.database.ArtistPrevResult
import com.poulastaa.kyoku.data.model.database.PlaylistPrevResult
import com.poulastaa.kyoku.data.model.database.PlaylistWithSongs
import com.poulastaa.kyoku.data.model.database.table.AlbumPrevTable
import com.poulastaa.kyoku.data.model.database.table.AlbumPreviewSongRelationTable
import com.poulastaa.kyoku.data.model.database.table.AlbumTable
import com.poulastaa.kyoku.data.model.database.table.ArtistMixTable
import com.poulastaa.kyoku.data.model.database.table.ArtistPrevTable
import com.poulastaa.kyoku.data.model.database.table.ArtistPreviewSongRelation
import com.poulastaa.kyoku.data.model.database.table.DailyMixPrevTable
import com.poulastaa.kyoku.data.model.database.table.DailyMixTable
import com.poulastaa.kyoku.data.model.database.table.FavouriteTable
import com.poulastaa.kyoku.data.model.database.table.FevArtistsMixPreviewTable
import com.poulastaa.kyoku.data.model.database.table.PinnedTable
import com.poulastaa.kyoku.data.model.database.table.PlaylistTable
import com.poulastaa.kyoku.data.model.database.table.RecentlyPlayedPrevTable
import com.poulastaa.kyoku.data.model.database.table.SongAlbumRelationTable
import com.poulastaa.kyoku.data.model.database.table.SongPlaylistRelationTable
import com.poulastaa.kyoku.data.model.database.table.SongPreviewTable
import com.poulastaa.kyoku.data.model.database.table.SongTable
import com.poulastaa.kyoku.data.model.screens.common.UiAlbumPrev
import com.poulastaa.kyoku.data.model.screens.home.HomeUiSavedAlbumPrev
import com.poulastaa.kyoku.data.model.screens.home.HomeUiSongPrev
import com.poulastaa.kyoku.data.model.screens.library.Artist
import com.poulastaa.kyoku.data.model.screens.song_view.UiPlaylistSong
import com.poulastaa.kyoku.data.model.screens.song_view.UiSong
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSong(song: SongTable): Long

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylist(playlist: PlaylistTable): Long

    @Transaction
    @Query(
        """ SELECT songtable.id , songtable.coverimage, songtable.title, songtable.artist,
            PlaylistTable.name FROM songtable
            JOIN SongPlaylistRelationTable ON songtable.id = SongPlaylistRelationTable.songId
            JOIN PlaylistTable ON SongPlaylistRelationTable.playlistId = PlaylistTable.id
            """
    )
    fun getAllPlaylist(): Flow<List<PlaylistWithSongs>>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSongPlaylistRelation(data: SongPlaylistRelationTable)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertIntoSongPrev(data: SongPreviewTable): Long

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBatchIntoSongPrev(data: List<SongPreviewTable>): List<Long>


    @Transaction
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertIntoFevArtistMixPrev(data: FevArtistsMixPreviewTable)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertIntoAlbumPrev(data: AlbumPrevTable): Long

    @Transaction
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertIntoAlbumPrevSongRelationTable(data: AlbumPreviewSongRelationTable)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertIntoArtist(data: ArtistPrevTable): Long

    @Transaction
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertIntoArtistPrevSongRelationTable(data: ArtistPreviewSongRelation)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIntoDailyMixPrevTable(data: List<DailyMixPrevTable>)

    @Query("select * from AlbumPrevTable limit 1")
    suspend fun isFirstOpen(): List<AlbumPrevTable>

    @Query("select * from PlaylistTable limit 1")
    suspend fun ifNewUserCheck_1(): List<PlaylistTable>

    @Query("select * from ALBUMTABLE limit 1")
    suspend fun ifNewUserCheck_2(): List<AlbumTable>

    @Query("select * from FavouriteTable limit 1")
    suspend fun isNewUserCheck_3(): List<FavouriteTable>


    @Transaction
    @Query("select * from fevartistsmixpreviewtable")
    fun readFevArtistPrev(): Flow<List<FevArtistsMixPreviewTable>>

    @Transaction
    @Query(
        """select SongPreviewTable.id ,SongPreviewTable.title , SongPreviewTable.artist ,  SongPreviewTable.coverImage ,
              AlbumPrevTable.name , AlbumPrevTable.albumId  from SongPreviewTable 
            join albumpreviewsongrelationtable on albumpreviewsongrelationtable.songId = SongPreviewTable.id
            join AlbumPrevTable on AlbumPrevTable.id = albumpreviewsongrelationtable.albumId
            where AlbumPrevTable.id in ( 
                select albumId from albumpreviewsongrelationtable
            ) order by AlbumPrevTable.id"""
    )
    fun readAllAlbumPrev(): Flow<List<AlbumPrevResult>>


    @Transaction
    @Query(
        """
        select SongPreviewTable.id ,SongPreviewTable.title ,  SongPreviewTable.coverImage , 
            ArtistPrevTable.name , ArtistPrevTable.id as artistId , ArtistPrevTable.coverImage as imageUrl  from SongPreviewTable
            join ArtistPreviewSongRelation on ArtistPreviewSongRelation.songId = SongPreviewTable.id
            join ArtistPrevTable on ArtistPrevTable.id = ArtistPreviewSongRelation.artistId
            where ArtistPrevTable.id in (
                select artistId from ArtistPrevTable
        ) order by ArtistPrevTable.id
    """
    )
    fun readAllArtistPrev(): Flow<List<ArtistPrevResult>>


    @Query(
        """
        select SongPreviewTable.coverImage  from SongPreviewTable 
        join DailyMixPrevTable on DailyMixPrevTable.id = SongPreviewTable.id
        where DailyMixPrevTable.id
    """
    )
    suspend fun readDailyMixPrevUrls(): List<String>

    @Transaction
    @Query(
        """
        select PlaylistTable.id , PlaylistTable.name , SongTable.coverImage  from PlaylistTable
        join SongPlaylistRelationTable on SongPlaylistRelationTable.playlistId = PlaylistTable.id
        join SongTable on SongTable.id = SongPlaylistRelationTable.songId
        where PlaylistTable.id in (
            select playlistId from SongPlaylistRelationTable
        ) order by PlaylistTable.points desc
    """
    )
    fun readPreviewPlaylist(): Flow<List<PlaylistPrevResult>>

    @Transaction
    @Query(
        """
        select songpreviewtable.id , songpreviewtable.title , songpreviewtable.artist , songpreviewtable.coverImage from songpreviewtable
        join RecentlyPlayedPrevTable on RecentlyPlayedPrevTable.songId = songpreviewtable.id
        where RecentlyPlayedPrevTable.songId in (
            select songId from RecentlyPlayedPrevTable order by id desc
        )
    """
    )
    fun redRecentlyPlayed(): Flow<List<HomeUiSongPrev>>

    @Query("select count(*) from FavouriteTable")
    suspend fun readFavouritePrev(): Long

    @Transaction
    @Query(
        """
        select SongTable.coverImage  , SongTable.album  from SongTable
        join SongAlbumRelationTable on SongAlbumRelationTable.songId = SongTable.id
        join AlbumTable on AlbumTable.id = SongAlbumRelationTable.albumId
        where SongAlbumRelationTable.albumId in (
            select id from AlbumTable
        ) group by AlbumTable.name order by AlbumTable.points desc limit 2
    """
    )
    fun radSavedAlbumPrev(): Flow<List<HomeUiSavedAlbumPrev>>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIntoFavourite(data: FavouriteTable)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIntoAlbum(data: AlbumTable): Long

    @Transaction
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertIntoSongAlbumRelationTable(data: SongAlbumRelationTable)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertIntoRecentlyPlayedPrevTable(data: RecentlyPlayedPrevTable)

    @Transaction
    @Query(
        """
        select AlbumTable.id , AlbumTable.name , SongTable.coverImage from SongTable
        join SongAlbumRelationTable on SongAlbumRelationTable.songId = SongTable.id
        join AlbumTable on AlbumTable.id = SongAlbumRelationTable.albumId
        where AlbumTable.id order by AlbumTable.points desc
    """
    )
    fun readAllAlbum(): Flow<List<UiAlbumPrev>>

    @Transaction
    @Query("select * from ArtistPrevTable")
    fun readAllArtist(): Flow<List<Artist>>

    @Query(
        """
        select id from PinnedTable where PinnedTable.playlistId = (
            select id from PlaylistTable where name = :name
        )
    """
    )
    suspend fun checkIfPlaylistIsPinned(name: String): Long?


    @Query(
        """
        select id from PinnedTable where PinnedTable.albumId = (
            select id from AlbumTable where name = :name
        )
    """
    )
    suspend fun checkIfAlbumIsPinned(name: String): Long?

    @Query(
        """
        select id from PinnedTable where PinnedTable.artistId = (
            select id from ArtistPrevTable where name = :name
        )
    """
    )
    suspend fun checkIfArtistPinned(name: String): Long?

    @Transaction
    @Query("select id from PlaylistTable where name = :name")
    suspend fun getIdOfPlaylist(name: String): Long?

    @Transaction
    @Query("select id from ArtistPrevTable where name = :name")
    suspend fun getIdOfArtist(name: String): Long?

    @Transaction
    @Query("select id from AlbumTable where name = :name")
    suspend fun getIdOfAlbum(name: String): Long?

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToPinnedTable(data: PinnedTable)

    @Transaction
    @Query("delete from PinnedTable where playlistId = :id")
    suspend fun removePlaylistIdFromPinnedTable(id: Long)

    @Transaction
    @Query("delete from PinnedTable where artistId = :id")
    suspend fun removeArtistIdFromPinnedTable(id: Long)

    @Transaction
    @Query("delete from PinnedTable where albumId = :id")
    suspend fun removeAlbumIdFromPinnedTable(id: Long)


    // read from pinned table
    @Transaction
    @Query(
        """
        select PlaylistTable.id , PlaylistTable.name , SongTable.coverImage  from PlaylistTable
        join SongPlaylistRelationTable on SongPlaylistRelationTable.playlistId = PlaylistTable.id
        join SongTable on SongTable.id = SongPlaylistRelationTable.songId
        where PlaylistTable.id in (
            select playlistId from PinnedTable
        ) order by PlaylistTable.points desc
    """
    )
    fun readPinnedPlaylist(): Flow<List<PlaylistPrevResult>>

    @Transaction
    @Query(
        """
        select SongTable.id, SongTable.album as name , SongTable.coverImage from SongTable 
        join SongAlbumRelationTable on SongAlbumRelationTable.songId = SongTable.id
        join PinnedTable on  PinnedTable.albumId = SongAlbumRelationTable.albumId
        where PinnedTable.albumId
    """
    )
    fun readPinnedAlbum(): Flow<List<UiAlbumPrev>>

    @Transaction
    @Query(
        """
        select ArtistPrevTable.id , ArtistPrevTable.name , ArtistPrevTable.coverImage 
        from ArtistPrevTable 
        join PinnedTable on ArtistPrevTable.id = PinnedTable.artistId
        where PinnedTable.artistId
    """
    )
    fun readPinnedArtist(): Flow<List<Artist>>
    // read from pinned table end


    // remove playlist album , artists
    @Transaction
    @Query("delete from PlaylistTable where id = :id")
    fun deletePlaylist(id: Long)

    @Transaction
    @Query("delete from AlbumTable where id = :id")
    fun deleteAlbum(id: Long)

    @Transaction
    @Query("delete from ArtistPrevTable where id = :id")
    fun deleteArtist(id: Long)

    @Transaction
    @Query("delete from FavouriteTable")
    fun deleteFavourites()


    // songView screen query
    @Query(
        """
        select SongTable.id , PlaylistTable.name , SongTable.title , SongTable.artist, SongTable.album , SongTable.coverImage from SongTable 
        join SongPlaylistRelationTable on SongPlaylistRelationTable.songId = SongTable.id
        join PlaylistTable on PlaylistTable.id = SongPlaylistRelationTable.playlistId
        where PlaylistTable.id = :id
    """
    )
    fun getPlaylist(id: Long): Flow<List<UiPlaylistSong>>

    @Query(
        """
        select SongTable.id , SongTable.title , SongTable.artist , SongTable.album , SongTable.coverImage from SongTable
        join SongAlbumRelationTable on SongAlbumRelationTable.songId = SongTable.id
        join AlbumTable on AlbumTable.id = SongAlbumRelationTable.albumId
        where AlbumTable.name = :name
    """
    )
    suspend fun getAlbum(name: String): List<UiSong>

    @Query(
        """
        select SongTable.id , SongTable.title , SongTable.artist , SongTable.album , SongTable.coverImage from SongTable
        join FavouriteTable on FavouriteTable.songId = SongTable.id
        where FavouriteTable.songId
    """
    )
    suspend fun getAllFavouriteSongs(): List<UiSong>

    @Query("select coverImage from ArtistPrevTable where id = :id")
    suspend fun getArtistCoverImage(id: Long): String

    @Query("select count(*) from DailyMixTable")
    suspend fun checkIfDailyMixTableEmpty(): Long

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIntoDailyMix(entrys: List<DailyMixTable>)

    @Query("select * from DailyMixTable")
    fun readAllDailyMix(): Flow<List<DailyMixTable>>

    @Query("select count(*) from ArtistMixTable")
    suspend fun checkIfArtistMixIsEmpty(): Long

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIntoArtistMix(entrys: List<ArtistMixTable>)

    @Query("select * from ArtistMixTable")
    fun readAllArtistMix(): Flow<List<ArtistMixTable>>

    // remove all
    @Query("delete from AlbumPrevTable")
    suspend fun dropAlbumPrevTable()

    @Query("delete from  AlbumPreviewSongRelationTable")
    suspend fun dropAlbumPrevSongTable()

    @Query("delete from  AlbumTable")
    suspend fun dropAlbumTable()

    @Query("delete from  ArtistPrevTable")
    suspend fun dropArtistPrevTable()

    @Query("delete from  ArtistPreviewSongRelation")
    suspend fun dropArtistPrevSongTable()

    @Query("delete from  DailyMixPrevTable")
    suspend fun dropDailyMixPrevTable()

    @Query("delete from  DailyMixTable")
    suspend fun dropDailyMixTable()

    @Query("delete from ArtistMixTable")
    suspend fun dropArtistMixTable()

    @Query("delete from  FavouriteTable")
    suspend fun dropFavouriteTable()

    @Query("delete from  FevArtistsMixPreviewTable")
    suspend fun dropFavArtistMixPrevTable()

    @Query("delete from  PinnedTable")
    suspend fun dropPinnedTable()

    @Query("delete from  PlaylistTable")
    suspend fun dropPlaylistTable()

    @Query("delete from  RecentlyPlayedPrevTable")
    suspend fun dropRecentlyPlayedPrevTable()

    @Query("delete from  SongAlbumRelationTable")
    suspend fun dropSongAlbumTable()

    @Query("delete from  SongPlaylistRelationTable")
    suspend fun dropSongPlaylistTable()

    @Query("delete from  SongPreviewTable")
    suspend fun dropSongPrevTable()

    @Query("delete from  SongTable")
    suspend fun dropSongTable()
}
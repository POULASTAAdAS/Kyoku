package com.poulastaa.kyoku.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.poulastaa.kyoku.data.model.database.ArtistPrevResult
import com.poulastaa.kyoku.data.model.database.PinnedId
import com.poulastaa.kyoku.data.model.database.PlaylistPrevResult
import com.poulastaa.kyoku.data.model.database.SongInfo
import com.poulastaa.kyoku.data.model.database.table.AlbumSongTable
import com.poulastaa.kyoku.data.model.database.table.AlbumTable
import com.poulastaa.kyoku.data.model.database.table.ArtistMixTable
import com.poulastaa.kyoku.data.model.database.table.ArtistPreviewSongRelation
import com.poulastaa.kyoku.data.model.database.table.ArtistTable
import com.poulastaa.kyoku.data.model.database.table.DailyMixPrevTable
import com.poulastaa.kyoku.data.model.database.table.DailyMixTable
import com.poulastaa.kyoku.data.model.database.table.FavouriteSongTable
import com.poulastaa.kyoku.data.model.database.table.FevArtistOrDailyMixPreviewTable
import com.poulastaa.kyoku.data.model.database.table.MixType
import com.poulastaa.kyoku.data.model.database.table.PinnedTable
import com.poulastaa.kyoku.data.model.database.table.PlaylistSongTable
import com.poulastaa.kyoku.data.model.database.table.PlaylistTable
import com.poulastaa.kyoku.data.model.database.table.RecentlyPlayedPrevTable
import com.poulastaa.kyoku.data.model.database.table.SongAlbumRelationTable
import com.poulastaa.kyoku.data.model.database.table.SongPlaylistRelationTable
import com.poulastaa.kyoku.data.model.database.table.SongPreviewTable
import com.poulastaa.kyoku.data.model.database.table.prev.ArtistSongTable
import com.poulastaa.kyoku.data.model.database.table.prev.PreviewAlbumTable
import com.poulastaa.kyoku.data.model.screens.common.UiAlbumPrev
import com.poulastaa.kyoku.data.model.screens.home.HomeUiSavedAlbumPrev
import com.poulastaa.kyoku.data.model.screens.home.HomeUiSongPrev
import com.poulastaa.kyoku.data.model.screens.library.Artist
import com.poulastaa.kyoku.data.model.screens.song_view.UiPlaylistSong
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
//    @Transaction
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    suspend fun insertSong(song: SongTable): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIntoPlaylistSongTable(entrys: List<PlaylistSongTable>): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIntoSongPlaylistRelation(entrys: List<SongPlaylistRelationTable>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylist(playlist: PlaylistTable): Long

    @Transaction
    @Query(
        """ select PlaylistSongTable.title ,  PlaylistSongTable.artist , PlaylistSongTable.coverImage ,PlaylistTable.name as playlistName
            from PlaylistSongTable 
            join SongPlaylistRelationTable on SongPlaylistRelationTable.songId = PlaylistSongTable.songId
            join PlaylistTable on PlaylistTable.id = SongPlaylistRelationTable.playlistId 
            order by PlaylistTable.id
            """
    )
    fun getAllPlaylist(): Flow<List<SongInfo>>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSongPlaylistRelation(data: SongPlaylistRelationTable)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertIntoSongPrev(data: SongPreviewTable): Long

    @Query("select id from PlaylistTable where playlistId = :playlistId")
    suspend fun getPlaylistInternalId(playlistId: Long): Long?

    @Query("select id from AlbumTable where albumId = :albumId")
    suspend fun getAlbumInternalId(albumId: Long): Long?

    @Query("select artistId from ArtistTable where artistId = :artistId")
    suspend fun getArtistInternalId(artistId: Int): Long?

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIntoFevArtistOrDailyMixPrev(entrys: List<FevArtistOrDailyMixPreviewTable>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIntoPrevAlbum(entrys: List<PreviewAlbumTable>)


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIntoArtist(entry: ArtistTable): Long?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIntoArtistSong(entry: ArtistSongTable): Long?


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIntoArtist(entrys: List<ArtistTable>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIntoArtistSong(entrys: List<ArtistSongTable>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIntoArtistPrevSongRelationTable(data: ArtistPreviewSongRelation)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIntoDailyMixPrevTable(data: List<DailyMixPrevTable>)

    @Query("select * from PreviewAlbumTable limit 1")
    suspend fun isFirstOpen(): List<PreviewAlbumTable>

    @Query("select * from PlaylistTable limit 1")
    suspend fun ifNewUserCheck_1(): List<PlaylistTable>

    @Query("select * from AlbumTable limit 1")
    suspend fun ifNewUserCheck_2(): List<AlbumTable>

    @Query("select * from FavouriteSongTable limit 1")
    suspend fun isNewUserCheck_3(): List<FavouriteSongTable>


    @Query("select coverImage from FevArtistOrDailyMixPreviewTable where type = :type  order by random() limit 4")
    fun readFevArtistMixPrev(type: MixType = MixType.ARTIST_MIX): List<String>

    @Query("select coverImage from FevArtistOrDailyMixPreviewTable where type = :type  order by random() limit 4")
    suspend fun readDailyMixPrevUrls(type: MixType = MixType.DAILY_MIX): List<String>

    @Transaction
    @Query("select * from PreviewAlbumTable")
    fun readAllAlbumPrev(): Flow<List<PreviewAlbumTable>>


    @Transaction
    @Query(
        """
        select ArtistTable.artistId , ArtistTable.name , ArtistTable.coverImage as artisUrl, 
        ArtistSongTable.songId , ArtistSongTable.title , ArtistSongTable.coverImage 
        from ArtistPreviewSongRelation
        join ArtistTable on ArtistTable.artistId = ArtistPreviewSongRelation.artistId
        join ArtistSongTable on ArtistSongTable.id = ArtistPreviewSongRelation.songId order by ArtistTable.artistId
    """
    )
    fun readAllArtistPrev(): Flow<List<ArtistPrevResult>>


    @Transaction
    @Query(
        """
        select PlaylistTable.playlistId, PlaylistTable.name , PlaylistSongTable.coverImage from PlaylistSongTable
        join SongPlaylistRelationTable on SongPlaylistRelationTable.songId =  PlaylistSongTable.songId
        join PlaylistTable on PlaylistTable.id = SongPlaylistRelationTable.playlistId order by PlaylistTable.points
    """
    )
    fun readPreviewPlaylist(): Flow<List<PlaylistPrevResult>>

    @Transaction
    @Query(
        """
        select songpreviewtable.songId as id , songpreviewtable.title , songpreviewtable.artist , songpreviewtable.coverImage from songpreviewtable
        join RecentlyPlayedPrevTable on RecentlyPlayedPrevTable.songId = songpreviewtable.id
        where RecentlyPlayedPrevTable.songId in (
            select songId from RecentlyPlayedPrevTable order by id desc
        )
    """
    )
    fun redRecentlyPlayed(): Flow<List<HomeUiSongPrev>>

    @Query("select count(*) from FavouriteSongTable")
    suspend fun readFavouritePrev(): Long

    @Query(
        """
        select AlbumTable.albumId , AlbumTable.name , AlbumSongTable.coverImage 
        from AlbumSongTable
        join SongAlbumRelationTable on SongAlbumRelationTable.songId =  AlbumSongTable.id
        join AlbumTable on AlbumTable.id = SongAlbumRelationTable.albumId group by AlbumTable.id 
    """
    )
    fun radSavedAlbumPrev(): Flow<List<HomeUiSavedAlbumPrev>>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIntoFavourite(entrys: List<FavouriteSongTable>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIntoAlbum(data: AlbumTable): Long?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insetIntoAlbumSongTable(data: AlbumSongTable): Long?

    @Transaction
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertIntoSongAlbumRelationTable(data: SongAlbumRelationTable)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertIntoRecentlyPlayedPrevTable(entrys: List<RecentlyPlayedPrevTable>)

    @Query(
        """
        SELECT  AlbumTable.albumId as id, AlbumTable.name, AlbumSongTable.coverImage
        FROM  SongAlbumRelationTable
        JOIN  AlbumTable ON AlbumTable.id = SongAlbumRelationTable.albumId
        JOIN AlbumSongTable ON AlbumSongTable.id = SongAlbumRelationTable.songId 
        group by AlbumTable.albumId order by AlbumTable.points desc
    """
    )
    fun readAllAlbumPreview(): Flow<List<UiAlbumPrev>>

    @Transaction
    @Query("select * from ArtistTable")
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
            select id from ArtistTable where name = :name
        )
    """
    )
    suspend fun checkIfArtistPinned(name: String): Long?

    @Transaction
    @Query("select id, playlistId as originalId from PlaylistTable where name = :name")
    suspend fun getIdOfPlaylist(name: String): PinnedId

    @Transaction
    @Query("select artistId from ArtistTable where name = :name")
    suspend fun getIdOfArtist(name: String): Long

    @Transaction
    @Query("select id , albumId as originalId from AlbumTable where name = :name")
    suspend fun getIdOfAlbum(name: String): PinnedId

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
    @Query(
        """
        select  PlaylistTable.playlistId ,  PlaylistTable.name , PlaylistSongTable.coverImage 
        from PlaylistSongTable
        join SongPlaylistRelationTable on SongPlaylistRelationTable.songId = PlaylistSongTable.songId
        join PlaylistTable on PlaylistTable.id = SongPlaylistRelationTable.playlistId
        join PinnedTable on PinnedTable.playlistId = PlaylistTable.id
    """
    )
    fun readPinnedPlaylist(): Flow<List<PlaylistPrevResult>>

    @Query(
        """
        select AlbumTable.albumId as id, AlbumTable.name, AlbumSongTable.coverImage from AlbumSongTable
        join SongAlbumRelationTable on SongAlbumRelationTable.songId = AlbumSongTable.id
        join AlbumTable on AlbumTable.id = SongAlbumRelationTable.albumId 
        join PinnedTable on PinnedTable.albumId = AlbumTable.id
        group by AlbumTable.albumId order by AlbumTable.points desc
    """
    )
    fun readPinnedAlbum(): Flow<List<UiAlbumPrev>>

    @Transaction
    @Query(
        """
        select ArtistTable.artistId , ArtistTable.name , ArtistTable.coverImage 
        from ArtistTable 
        join PinnedTable on ArtistTable.artistId = PinnedTable.artistId
        where PinnedTable.artistId
    """
    )
    fun readPinnedArtist(): Flow<List<Artist>>
    // read from pinned table end


    // remove playlist album , artists
    @Query("delete from PlaylistTable where id = :id")
    fun deletePlaylist(id: Long)

    @Query("delete from AlbumTable where id = :id")
    fun deleteAlbum(id: Long)

    @Query("delete from ArtistTable where artistId = :id")
    fun deleteArtist(id: Long)

    @Query("delete from FavouriteSongTable")
    fun deleteFavourites()

    // songView screen query

    @Query("select name from PlaylistTable where playlistId = :id")
    fun getPlaylistName(id: Long): String

    @Query(
        """
        select PlaylistSongTable.songId , PlaylistSongTable.title , PlaylistSongTable.artist , 
        PlaylistSongTable.coverImage , PlaylistSongTable.masterPlaylistUrl , PlaylistSongTable.totalTime
        from PlaylistSongTable
        join SongPlaylistRelationTable on SongPlaylistRelationTable.songId = PlaylistSongTable.songId
        join PlaylistTable on PlaylistTable.id = SongPlaylistRelationTable.playlistId
        where PlaylistTable.playlistId  = :id
    """
    )
    fun getPlaylist(id: Long): Flow<List<UiPlaylistSong>>


    @Query("select name from AlbumTable where albumId = :id")
    suspend fun getAlbumName(id: Long): String

    @Query(
        """
        select AlbumSongTable.songId , AlbumSongTable.title , AlbumSongTable.artist , 
        AlbumSongTable.coverImage , AlbumSongTable.masterPlaylistUrl , AlbumSongTable.totalTime from AlbumSongTable
        join SongAlbumRelationTable on SongAlbumRelationTable.songId = AlbumSongTable.id
        join AlbumTable on AlbumTable.id = SongAlbumRelationTable.albumId
        where AlbumTable.albumId = :id
    """
    )
    suspend fun getAlbum(id: Long): List<UiPlaylistSong>

    @Query("select songId , title , artist , coverImage , masterPlaylistUrl , totalTime from FavouriteSongTable")
    fun getAllFavouriteSongs(): Flow<List<UiPlaylistSong>>

    @Query("select coverImage from ArtistTable where artistId = :id")
    suspend fun getArtistCoverImage(id: Long): String

    @Query("select count(*) from DailyMixTable")
    suspend fun checkIfDailyMixTableEmpty(): Long

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIntoDailyMix(entrys: List<DailyMixTable>)

    @Query("select songId , title , artist , coverImage , masterPlaylistUrl , totalTime from DailyMixTable")
    fun readAllDailyMix(): Flow<List<UiPlaylistSong>>

    @Query("select count(*) from ArtistMixTable")
    suspend fun checkIfArtistMixIsEmpty(): Long

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIntoArtistMix(entrys: List<ArtistMixTable>)

    @Query("select songId , title , artist , coverImage , masterPlaylistUrl , totalTime from ArtistMixTable")
    fun readAllArtistMix(): Flow<List<UiPlaylistSong>>

//    @Query(
//        """
//        select SongTable.songId from SongTable
//        join FavouriteTable on  FavouriteTable.songId = SongTable.id
//        where FavouriteTable.songId
//    """
//    )
//    suspend fun getAllFavouriteSongId(): List<Long>

//    @Query("select id from SongTable where songId = :songId")
//    suspend fun getAllIdOnSongId(songId: Long): List<Long>

    @Query("select id from AlbumTable where albumId = :albumId")
    suspend fun checkIfAlbumAlreadyInLibrary(albumId: Long): Long?

    @Query("select id from DailyMixTable limit 1")
    suspend fun isDailyMixDownloaded(): List<Int>

    @Query("select songId from DailyMixTable")
    suspend fun getSongIdListOfDailyMix(): List<Long>

    @Query("select id from playlisttable where name = :name")
    suspend fun playlistNameDuplicityCheck(name: String): List<Long>

    @Query("select id from ArtistMixTable limit 1")
    suspend fun isArtistMixDownloaded(): List<Long>

    @Query("select songId from ArtistMixTable")
    suspend fun getSongIdListOfArtistMix(): List<Long>

//    @Query("delete from FavouriteTable where songId in (:listOfId)")
//    suspend fun deleteFromFavourite(listOfId: List<Long>)


    // remove all
    @Query("delete from  AlbumTable")
    suspend fun dropAlbumTable()

    @Query("delete from  ArtistTable")
    suspend fun dropArtistTable()

    @Query("delete from  ArtistPreviewSongRelation")
    suspend fun dropArtistPrevSongTable()

    @Query("delete from  DailyMixPrevTable")
    suspend fun dropDailyMixPrevTable()

    @Query("delete from  DailyMixTable")
    suspend fun dropDailyMixTable()

    @Query("delete from ArtistMixTable")
    suspend fun dropArtistMixTable()

    @Query("delete from  FevArtistOrDailyMixPreviewTable")
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
}
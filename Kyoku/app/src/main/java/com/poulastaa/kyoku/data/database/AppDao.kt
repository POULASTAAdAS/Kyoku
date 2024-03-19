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
import com.poulastaa.kyoku.data.model.database.table.AlbumPreviewSongRelationTable
import com.poulastaa.kyoku.data.model.database.table.AlbumTable
import com.poulastaa.kyoku.data.model.database.table.ArtistPreviewSongRelation
import com.poulastaa.kyoku.data.model.database.table.ArtistTable
import com.poulastaa.kyoku.data.model.database.table.DailyMixPrevTable
import com.poulastaa.kyoku.data.model.database.table.FevArtistsMixPreviewTable
import com.poulastaa.kyoku.data.model.database.table.PlaylistTable
import com.poulastaa.kyoku.data.model.database.table.SongPlaylistRelationTable
import com.poulastaa.kyoku.data.model.database.table.SongPreviewTable
import com.poulastaa.kyoku.data.model.database.table.SongTable
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Transaction
    @Query(
        "SELECT songtable.id , songtable.coverimage, songtable.title, songtable.artist," +
                " PlaylistTable.name " +
                "FROM songtable JOIN SongPlaylistRelationTable ON songtable.id = SongPlaylistRelationTable.songId " +
                "JOIN PlaylistTable ON SongPlaylistRelationTable.playlistId = PlaylistTable.id"
    )
    fun getAllPlaylist(): Flow<List<PlaylistWithSongs>>

    @Transaction
    @Query("select * from songtable")
    fun getAllSong(): Flow<List<SongTable>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSong(song: SongTable): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylist(playlist: PlaylistTable): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSongPlaylistRelation(data: SongPlaylistRelationTable)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertIntoSongPrev(data: SongPreviewTable): Long

    @Transaction
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertIntoFevArtistMixPrev(data: FevArtistsMixPreviewTable)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertIntoAlbum(data: AlbumTable): Long

    @Transaction
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertIntoAlbumPrevSongRelationTable(data: AlbumPreviewSongRelationTable)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertIntoArtist(data: ArtistTable): Long

    @Transaction
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertIntoArtistPrevSongRelationTable(data: ArtistPreviewSongRelation)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertIntoDailyMixPrevTable(data: DailyMixPrevTable)

    @Query("select * from albumtable limit 1") // fetching all entry is un-necessary
    suspend fun checkIfNewUser(): List<AlbumTable> // could had any other table related to homeResponse


    @Query("select * from fevartistsmixpreviewtable")
    fun readFevArtistPrev(): Flow<List<FevArtistsMixPreviewTable>>

    @Transaction
    @Query(
        """select SongPreviewTable.id ,SongPreviewTable.title , SongPreviewTable.artist ,  SongPreviewTable.coverImage ,  albumtable.name from SongPreviewTable 
            join albumpreviewsongrelationtable on albumpreviewsongrelationtable.songId = SongPreviewTable.id
            join albumtable on albumtable.id = albumpreviewsongrelationtable.albumId
            where albumtable.id in ( 
                select albumId from albumpreviewsongrelationtable
            ) order by albumtable.id"""
    )
    fun readAllAlbumPrev(): Flow<List<AlbumPrevResult>>


    @Transaction
    @Query(
        """
        select SongPreviewTable.id ,SongPreviewTable.title ,  SongPreviewTable.coverImage , ArtistTable.name , ArtistTable.imageUrl  from SongPreviewTable
            join ArtistPreviewSongRelation on ArtistPreviewSongRelation.songId = SongPreviewTable.id
            join ArtistTable on ArtistTable.id = ArtistPreviewSongRelation.artistId
            where ArtistTable.id in (
                select artistId from ArtistTable
        ) order by ArtistTable.id
    """
    )
    fun readAllArtistPrev(): Flow<List<ArtistPrevResult>>

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
}
















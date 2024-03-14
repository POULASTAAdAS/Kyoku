package com.poulastaa.kyoku.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.poulastaa.kyoku.data.model.database.PlaylistWithSongs
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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIntoFevArtistMixPrev(data: FevArtistsMixPreviewTable)

    @Query("delete from fevartistsmixpreviewtable")
    fun deleteAllFromFevArtistMixPrevTable()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIntoAlbum(data: AlbumTable)

    @Query("delete from albumtable")
    fun deleteAllFromAlbum()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIntoArtist(data: ArtistTable): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIntoSongPrev(data: SongPreviewTable): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIntoArtistPrevSongRelationTable(data: ArtistPreviewSongRelation)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIntoDailyMixPrevTable(data: DailyMixPrevTable)

    @Query("select * from albumtable limit 1") // fetching all entry is un-necessary
    suspend fun checkIfNewUser(): List<AlbumTable> // could have any other table related to homeResponse


}
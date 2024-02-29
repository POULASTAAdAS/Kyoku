package com.poulastaa.kyoku.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.poulastaa.kyoku.data.model.database.PlaylistWithSongs
import com.poulastaa.kyoku.data.model.database.table.PlaylistTable
import com.poulastaa.kyoku.data.model.database.table.SongPlaylistRelationTable
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
}
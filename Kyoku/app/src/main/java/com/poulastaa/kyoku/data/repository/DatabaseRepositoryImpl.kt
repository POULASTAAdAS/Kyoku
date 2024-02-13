package com.poulastaa.kyoku.data.repository

import com.poulastaa.kyoku.data.database.AppDao
import com.poulastaa.kyoku.data.model.database.PlaylistWithSongs
import com.poulastaa.kyoku.data.model.database.table.PlaylistRelationTable
import com.poulastaa.kyoku.data.model.database.table.PlaylistTable
import com.poulastaa.kyoku.data.model.database.table.SongTable
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class DatabaseRepositoryImpl @Inject constructor(
    private val dao: AppDao
) {
    suspend fun insertSong(song: SongTable): Long = dao.insertSong(song)

    suspend fun insertSongIntoPlaylist(playlist: PlaylistTable) =
        dao.insertSongIntoPlaylist(playlist)

    suspend fun insertDataIntoPlaylistRelationTable(data: PlaylistRelationTable) =
        dao.insertDataIntoPlaylistRelationTable(data)

    fun getAllPlaylist(): Flow<List<PlaylistWithSongs>> = dao.getAllPlaylist()

    fun getAllSong(): Flow<List<SongTable>> = dao.getAllSong()
}
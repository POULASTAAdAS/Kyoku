package com.poulastaa.board.data.repository

import com.poulastaa.board.domain.import_playlist.ImportPlaylistLocalDatasource
import com.poulastaa.board.domain.import_playlist.ImportPlaylistRemoteDatasource
import com.poulastaa.board.domain.import_playlist.ImportPlaylistRepository
import com.poulastaa.board.domain.import_playlist.model.DtoPrevPlaylist
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.EmptyResult
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.asEmptyDataResult
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

internal class OfflineFirstImportPlaylistRepository @Inject constructor(
    private val local: ImportPlaylistLocalDatasource,
    private val remote: ImportPlaylistRemoteDatasource,
) : ImportPlaylistRepository {
    override suspend fun requestPlaylist(playlistId: String): EmptyResult<DataError.Network> {
        val result = remote.loadPlaylist(playlistId)

        if (result is Result.Success) local.savePlaylist(result.data)
        return result.asEmptyDataResult()
    }

    override fun loadAllPlaylist(): Flow<List<DtoPrevPlaylist>> = local.loadAllPlaylist()
}
package com.poulastaa.setup.data.repository.import_playlist

import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.EmptyResult
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.asEmptyDataResult
import com.poulastaa.core.domain.model.DtoPrevPlaylist
import com.poulastaa.core.domain.repository.LocalImportPlaylistDatasource
import com.poulastaa.setup.domain.repository.import_playlist.ImportPlaylistRepository
import com.poulastaa.setup.domain.repository.import_playlist.RemoteImportPlaylistDatasource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OnlineFirstImportPlaylistRepository @Inject constructor(
    private val local: LocalImportPlaylistDatasource,
    private val remote: RemoteImportPlaylistDatasource,
) : ImportPlaylistRepository {
    override fun loadAllPlaylist(): Flow<List<DtoPrevPlaylist>> = local.loadAllPlaylist()

    override suspend fun importPlaylist(playlistId: String): EmptyResult<DataError.Network> {
        val result = remote.importPlaylist(playlistId)
        if (result is Result.Success) local.storePlaylist(result.data)

        return result.asEmptyDataResult()
    }
}
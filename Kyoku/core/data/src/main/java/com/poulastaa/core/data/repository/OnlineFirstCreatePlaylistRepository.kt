package com.poulastaa.core.data.repository

import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.map
import com.poulastaa.core.domain.model.PlaylistId
import com.poulastaa.core.domain.repository.CreatePlaylistRepository
import com.poulastaa.core.domain.repository.LocalCreatePlaylistDatasource
import com.poulastaa.core.domain.repository.RemoteCreatePlaylistDatasource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class OnlineFirstCreatePlaylistRepository @Inject constructor(
    private val remote: RemoteCreatePlaylistDatasource,
    private val local: LocalCreatePlaylistDatasource,
    private val scope: CoroutineScope,
) : CreatePlaylistRepository {
    override suspend fun createPlaylist(name: String): Result<PlaylistId, DataError.Network> {
        val result = remote.createPlaylist(name)

        if (result is Result.Success) scope.launch {
            local.savePlaylist(result.data)
        }

        return result.map { it.id }
    }
}
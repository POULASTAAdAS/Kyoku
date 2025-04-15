package com.poulastaa.core.domain.repository

import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.model.PlaylistId

interface CreatePlaylistRepository {
    suspend fun createPlaylist(name: String): Result<PlaylistId, DataError.Network>
}
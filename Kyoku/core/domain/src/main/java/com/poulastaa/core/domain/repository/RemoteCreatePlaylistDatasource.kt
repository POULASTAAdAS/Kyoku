package com.poulastaa.core.domain.repository

import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.model.DtoPlaylist

interface RemoteCreatePlaylistDatasource {
    suspend fun createPlaylist(name: String): Result<DtoPlaylist, DataError.Network>
}
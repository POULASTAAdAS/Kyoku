package com.poulastaa.setup.domain

import com.poulastaa.common.domain.result.ApiError
import com.poulastaa.common.domain.result.AppResult
import com.poulastaa.setup.domain.model.DtoFullPlaylist

interface SetupRemoteDatasource {
    suspend fun importPlaylist(spotifyPlaylistId: String): AppResult<DtoFullPlaylist, ApiError>
}

package com.poulastaa.setup.domain

import com.poulastaa.common.domain.model.DtoFullPlaylist
import com.poulastaa.common.domain.result.ApiError
import com.poulastaa.common.domain.result.AppResult

interface SetupRemoteDatasource {
    suspend fun importPlaylist(spotifyPlaylistId: String, force: Boolean): AppResult<DtoFullPlaylist, ApiError>
    suspend fun getPlaylists(): AppResult<List<DtoFullPlaylist>, ApiError>
}

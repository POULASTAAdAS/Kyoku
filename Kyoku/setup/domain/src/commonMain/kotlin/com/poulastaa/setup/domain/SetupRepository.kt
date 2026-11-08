package com.poulastaa.setup.domain

import com.poulastaa.common.domain.model.DtoFullPlaylist
import com.poulastaa.common.domain.result.ApiError
import com.poulastaa.common.domain.result.AppResult
import com.poulastaa.common.domain.result.DataError

interface SetupRepository {
    suspend fun importPlaylist(spotifyPlaylistId: String): AppResult<DtoFullPlaylist, ApiError>
    suspend fun getSavedPlaylists(): AppResult<List<DtoFullPlaylist>, DataError>
}

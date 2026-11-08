package com.poulastaa.setup.domain

import com.poulastaa.common.domain.model.DtoFullPlaylist
import com.poulastaa.common.domain.result.ApiError
import com.poulastaa.common.domain.result.AppResult
import com.poulastaa.common.domain.result.DataError

interface SetupRepository {
    /**
     * @param force re-import even when the playlist was already imported; skips the duplicate checks.
     */
    suspend fun importPlaylist(spotifyPlaylistId: String, force: Boolean): AppResult<DtoFullPlaylist, ApiError>
    suspend fun getSavedPlaylists(): AppResult<List<DtoFullPlaylist>, DataError>
}

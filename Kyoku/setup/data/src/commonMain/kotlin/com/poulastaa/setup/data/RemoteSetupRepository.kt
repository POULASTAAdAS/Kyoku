package com.poulastaa.setup.data

import com.poulastaa.common.domain.model.DtoFullPlaylist
import com.poulastaa.common.domain.result.ApiError
import com.poulastaa.common.domain.result.AppResult
import com.poulastaa.common.domain.result.DataError
import com.poulastaa.common.domain.result.toErrorResponse
import com.poulastaa.setup.domain.SetupLocalDatasource
import com.poulastaa.setup.domain.SetupRemoteDatasource
import com.poulastaa.setup.domain.SetupRepository
import org.koin.core.annotation.Single

@Single(binds = [SetupRepository::class])
class RemoteSetupRepository(
    private val remote: SetupRemoteDatasource,
    private val local: SetupLocalDatasource,
) : SetupRepository {
    override suspend fun importPlaylist(
        spotifyPlaylistId: String,
        force: Boolean,
    ): AppResult<DtoFullPlaylist, ApiError> {
        // local first: skip the api call when this device already has the playlist
        if (!force) {
            val isImported = local.isSpotifyPlaylistImported(spotifyPlaylistId)
            if (isImported is AppResult.Success && isImported.response) return AppResult.Error(
                error = ApiError.Playlist.PLAYLIST_ALREADY_IMPORTED.toErrorResponse()
            )
        }

        val result = remote.importPlaylist(spotifyPlaylistId, force)
        if (result is AppResult.Success) local.savePlaylist(result.response, spotifyPlaylistId)

        return result
    }

    override suspend fun getSavedPlaylists(): AppResult<List<DtoFullPlaylist>, DataError> =
        local.getPlaylists()
}

package com.poulastaa.setup.data

import com.poulastaa.common.domain.model.DtoFullPlaylist
import com.poulastaa.common.domain.result.ApiError
import com.poulastaa.common.domain.result.AppResult
import com.poulastaa.common.domain.result.DataError
import com.poulastaa.setup.domain.SetupLocalDatasource
import com.poulastaa.setup.domain.SetupRemoteDatasource
import com.poulastaa.setup.domain.SetupRepository
import org.koin.core.annotation.Single

@Single(binds = [SetupRepository::class])
class RemoteSetupRepository(
    private val remote: SetupRemoteDatasource,
    private val local: SetupLocalDatasource,
) : SetupRepository {
    override suspend fun importPlaylist(spotifyPlaylistId: String): AppResult<DtoFullPlaylist, ApiError> {
        val result = remote.importPlaylist(spotifyPlaylistId)
        if (result is AppResult.Success) local.savePlaylist(result.response)

        return result
    }

    override suspend fun getSavedPlaylists(): AppResult<List<DtoFullPlaylist>, DataError> =
        local.getPlaylists()
}

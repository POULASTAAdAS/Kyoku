package com.poulastaa.setup.data

import com.poulastaa.common.domain.result.ApiError
import com.poulastaa.common.domain.result.AppResult
import com.poulastaa.setup.domain.SetupRemoteDatasource
import com.poulastaa.setup.domain.SetupRepository
import com.poulastaa.setup.domain.model.DtoFullPlaylist
import org.koin.core.annotation.Single

@Single(binds = [SetupRepository::class])
class RemoteSetupRepository(
    private val remote: SetupRemoteDatasource,
) : SetupRepository {
    override suspend fun importPlaylist(spotifyPlaylistId: String): AppResult<DtoFullPlaylist, ApiError> =
        remote.importPlaylist(spotifyPlaylistId)
}

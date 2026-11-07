package com.poulastaa.setup.network

import com.poulastaa.common.domain.model.DtoFullPlaylist
import com.poulastaa.common.domain.result.ApiError
import com.poulastaa.common.domain.result.AppResult
import com.poulastaa.common.domain.result.map
import com.poulastaa.common.network.ApiEndpoints
import com.poulastaa.common.network.ApiRequestType
import com.poulastaa.common.network.req
import com.poulastaa.setup.domain.SetupRemoteDatasource
import com.poulastaa.setup.network.model.FullPlaylistResponse
import com.poulastaa.setup.network.model.ImportPlaylistRequest
import io.ktor.client.HttpClient
import org.koin.core.annotation.Single

@Single(binds = [SetupRemoteDatasource::class])
class KtorSetupDatasource(
    private val client: HttpClient,
) : SetupRemoteDatasource {
    override suspend fun importPlaylist(spotifyPlaylistId: String): AppResult<DtoFullPlaylist, ApiError> =
        client.req<ImportPlaylistRequest, FullPlaylistResponse, ApiError.Network>(
            route = ApiEndpoints.Setup.IMPORT_PLAYLIST,
            type = ApiRequestType.POST,
            body = ImportPlaylistRequest(spotifyPlaylistId),
        ).map(FullPlaylistResponse::toDto)
}

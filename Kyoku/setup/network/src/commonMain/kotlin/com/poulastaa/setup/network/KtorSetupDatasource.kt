package com.poulastaa.setup.network

import com.poulastaa.common.domain.result.ApiError
import com.poulastaa.common.domain.result.AppResult
import com.poulastaa.common.domain.result.map
import com.poulastaa.common.network.ApiEndpoints
import com.poulastaa.common.network.ApiRequestType
import com.poulastaa.common.network.req
import com.poulastaa.setup.domain.SetupRemoteDatasource
import com.poulastaa.setup.domain.model.DtoFullPlaylist
import com.poulastaa.setup.network.model.FullPlaylistResponse
import io.ktor.client.HttpClient
import org.koin.core.annotation.Single

@Single(binds = [SetupRemoteDatasource::class])
class KtorSetupDatasource(
    private val client: HttpClient,
) : SetupRemoteDatasource {
    override suspend fun importPlaylist(spotifyPlaylistId: String): AppResult<DtoFullPlaylist, ApiError> =
        client.req<Unit, FullPlaylistResponse, ApiError.Network>(
            route = ApiEndpoints.Setup.IMPORT_PLAYLIST,
            type = ApiRequestType.GET,
            params = listOf(PLAYLIST_ID_PARAM to spotifyPlaylistId),
        ).map(FullPlaylistResponse::toDto)

    private companion object {
        const val PLAYLIST_ID_PARAM = "playlistId"
    }
}

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
import com.poulastaa.setup.network.model.UserPlaylistsResponse
import io.ktor.client.HttpClient
import org.koin.core.annotation.Single

@Single(binds = [SetupRemoteDatasource::class])
class KtorSetupDatasource(
    private val client: HttpClient,
) : SetupRemoteDatasource {
    override suspend fun importPlaylist(
        spotifyPlaylistId: String,
        force: Boolean,
    ): AppResult<DtoFullPlaylist, ApiError> =
        client.req<ImportPlaylistRequest, FullPlaylistResponse, ApiError.Playlist>(
            route = ApiEndpoints.Setup.IMPORT_PLAYLIST,
            type = ApiRequestType.POST,
            body = ImportPlaylistRequest(spotifyPlaylistId, force),
        ).map(FullPlaylistResponse::toDto)

    override suspend fun getPlaylists(): AppResult<List<DtoFullPlaylist>, ApiError> =
        client.req<Unit, UserPlaylistsResponse, ApiError.Playlist>(
            route = ApiEndpoints.Setup.GET_PLAYLISTS,
            type = ApiRequestType.GET,
        ).map { response -> response.playlists.map(FullPlaylistResponse::toDto) }
}

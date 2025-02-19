package com.poulastaa.setup.network.repository

import com.google.gson.Gson
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.map
import com.poulastaa.core.domain.model.DtoFullPlaylist
import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.network.ApiMethodType
import com.poulastaa.core.network.model.ResponseFullPlaylist
import com.poulastaa.core.network.req
import com.poulastaa.setup.domain.repository.import_playlist.RemoteImportPlaylistDatasource
import com.poulastaa.setup.network.model.ImportSpotifyPlaylistReq
import com.poulastaa.setup.network.toDtoPlaylist
import okhttp3.OkHttpClient
import javax.inject.Inject

class OkHttpImportPlaylistDatasource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
) : RemoteImportPlaylistDatasource {
    override suspend fun importPlaylist(playlistId: String): Result<DtoFullPlaylist, DataError.Network> {
        val response = client.req<ImportSpotifyPlaylistReq, ResponseFullPlaylist>(
            route = EndPoints.ImportSpotifyPlaylist.route,
            method = ApiMethodType.POST,
            gson = gson,
            body = ImportSpotifyPlaylistReq(
                playlistId = playlistId
            )
        )

        return response.map { it.toDtoPlaylist() }
    }
}
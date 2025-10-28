package com.poulastaa.baord.network.repository

import com.poulastaa.board.domain.import_playlist.ImportPlaylistRemoteDatasource
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.map
import com.poulastaa.core.domain.model.DtoFullPlaylist
import com.poulastaa.core.network.domain.model.Endpoints
import com.poulastaa.core.network.domain.model.ReqParam
import com.poulastaa.core.network.domain.model.response.ResponseFullPlaylist
import com.poulastaa.core.network.domain.repository.ApiRepository
import com.poulastaa.core.network.toDtoFullPlaylist
import javax.inject.Inject

internal class OkHttpImportPlaylistDatasource @Inject constructor(
    private val repo: ApiRepository,
) : ImportPlaylistRemoteDatasource {
    override suspend fun loadPlaylist(
        playlistId: String,
    ): Result<DtoFullPlaylist, DataError.Network> = repo.authReq<Unit, ResponseFullPlaylist>(
        route = Endpoints.ImportPlaylist,
        method = ApiRepository.Method.GET,
        type = ResponseFullPlaylist::class.java,
        body = null,
        params = listOf(
            ReqParam(
                key = "playlistId",
                value = playlistId
            )
        )
    ).map { it.toDtoFullPlaylist() }
}
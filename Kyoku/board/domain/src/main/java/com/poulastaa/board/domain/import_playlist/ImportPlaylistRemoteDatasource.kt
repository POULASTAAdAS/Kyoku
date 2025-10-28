package com.poulastaa.board.domain.import_playlist

import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.model.DtoFullPlaylist

interface ImportPlaylistRemoteDatasource {
    suspend fun loadPlaylist(playlistId: String): Result<DtoFullPlaylist, DataError.Network>
}
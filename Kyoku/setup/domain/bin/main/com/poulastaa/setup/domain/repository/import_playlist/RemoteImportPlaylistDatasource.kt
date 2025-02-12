package com.poulastaa.setup.domain.repository.import_playlist

import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.model.DtoFullPlaylist

interface RemoteImportPlaylistDatasource {
    suspend fun importPlaylist(playlistId: String): Result<DtoFullPlaylist, DataError.Network>
}
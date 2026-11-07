package com.poulastaa.setup.domain

import com.poulastaa.common.domain.model.DtoFullPlaylist
import com.poulastaa.common.domain.result.DataError
import com.poulastaa.common.domain.result.EmptyResponse

interface SetupLocalDatasource {
    suspend fun savePlaylist(playlist: DtoFullPlaylist): EmptyResponse<DataError>
}

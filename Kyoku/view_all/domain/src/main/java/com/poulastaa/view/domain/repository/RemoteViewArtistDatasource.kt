package com.poulastaa.view.domain.repository

import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.view.domain.model.DtoViewArtisPayload

interface RemoteViewArtistDatasource {
    suspend fun loadArtist(artistId: ArtistId): Result<DtoViewArtisPayload<DtoPrevArtist>, DataError.Network>
}
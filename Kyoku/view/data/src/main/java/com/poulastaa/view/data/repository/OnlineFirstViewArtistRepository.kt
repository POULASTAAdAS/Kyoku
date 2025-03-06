package com.poulastaa.view.data.repository

import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.view.domain.model.DtoViewArtisPayload
import com.poulastaa.view.domain.repository.ViewArtistRepository
import javax.inject.Inject

class OnlineFirstViewArtistRepository @Inject constructor() : ViewArtistRepository {
    override suspend fun loadArtist(artistId: ArtistId): Result<DtoViewArtisPayload, DataError.Network> {
        return Result.Error(DataError.Network.SERVER_ERROR)
    }
}
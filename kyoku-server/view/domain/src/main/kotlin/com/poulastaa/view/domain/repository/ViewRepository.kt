package com.poulastaa.view.domain.repository

import com.poulastaa.core.domain.model.ReqUserPayload
import com.poulastaa.core.domain.repository.ArtistId
import com.poulastaa.view.domain.model.ViewArtistPayload

interface ViewRepository {
    suspend fun getArtist(
        artistId: ArtistId,
        userPayload: ReqUserPayload,
    ): ViewArtistPayload?
}
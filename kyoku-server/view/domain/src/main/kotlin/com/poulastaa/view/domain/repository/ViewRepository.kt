package com.poulastaa.view.domain.repository

import com.poulastaa.core.domain.model.DtoViewOtherPayload
import com.poulastaa.core.domain.model.DtoViewType
import com.poulastaa.core.domain.model.ReqUserPayload
import com.poulastaa.core.domain.repository.ArtistId
import com.poulastaa.core.domain.repository.SongId
import com.poulastaa.view.domain.model.DtoViewArtistPayload

interface ViewRepository {
    suspend fun getArtist(
        artistId: ArtistId,
        userPayload: ReqUserPayload,
    ): DtoViewArtistPayload?

    suspend fun getViewTypeData(
        type: DtoViewType,
        otherId: Long,
        songIds: List<SongId>? = null,
        payload: ReqUserPayload,
    ): DtoViewOtherPayload?
}
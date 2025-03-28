package com.poulastaa.core.domain.repository

import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.DtoPrevArtist

interface LocalAllFromArtistDatasource {
    suspend fun getArtist(artistId: ArtistId): DtoPrevArtist?
}
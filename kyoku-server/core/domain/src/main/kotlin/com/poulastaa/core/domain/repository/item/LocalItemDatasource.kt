package com.poulastaa.core.domain.repository.item

import com.poulastaa.core.domain.model.DtoArtist
import com.poulastaa.core.domain.repository.ArtistId

interface LocalItemDatasource {
    suspend fun getArtistById(artistId: ArtistId): DtoArtist?
}
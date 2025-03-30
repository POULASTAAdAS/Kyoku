package com.poulastaa.item.domain.repository

import com.poulastaa.core.domain.model.DtoArtist
import com.poulastaa.core.domain.repository.ArtistId

interface ItemRepository {
    suspend fun getArtist(artistId: ArtistId): DtoArtist?
}
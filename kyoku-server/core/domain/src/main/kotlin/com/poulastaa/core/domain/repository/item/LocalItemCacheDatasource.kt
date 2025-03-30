package com.poulastaa.core.domain.repository.item

import com.poulastaa.core.domain.model.DtoArtist
import com.poulastaa.core.domain.repository.ArtistId

interface LocalItemCacheDatasource {
    fun setArtistById(artist: DtoArtist)
    fun cacheArtistById(artistId: ArtistId): DtoArtist?
}
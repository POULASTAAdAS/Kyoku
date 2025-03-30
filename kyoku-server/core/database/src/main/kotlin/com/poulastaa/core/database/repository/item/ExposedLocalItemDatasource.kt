package com.poulastaa.core.database.repository.item

import com.poulastaa.core.domain.model.DtoArtist
import com.poulastaa.core.domain.repository.ArtistId
import com.poulastaa.core.domain.repository.LocalCoreDatasource
import com.poulastaa.core.domain.repository.item.LocalItemCacheDatasource
import com.poulastaa.core.domain.repository.item.LocalItemDatasource

internal class ExposedLocalItemDatasource(
    private val core: LocalCoreDatasource,
    private val cache: LocalItemCacheDatasource,
) : LocalItemDatasource {
    override suspend fun getArtistById(artistId: ArtistId): DtoArtist? =
        cache.cacheArtistById(artistId) ?: core.getArtistOnId(artistId)?.also {
            cache.setArtistById(it)
        }
}
package com.poulastaa.item.data.repository

import com.poulastaa.core.domain.model.DtoArtist
import com.poulastaa.core.domain.repository.ArtistId
import com.poulastaa.core.domain.repository.item.LocalItemDatasource
import com.poulastaa.item.domain.repository.ItemRepository

internal class ItemRepositoryService(
    private val db: LocalItemDatasource,
) : ItemRepository {
    override suspend fun getArtist(artistId: ArtistId): DtoArtist? = db.getArtistById(artistId)
}
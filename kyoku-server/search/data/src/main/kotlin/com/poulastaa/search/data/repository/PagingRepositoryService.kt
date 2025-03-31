package com.poulastaa.search.data.repository

import com.poulastaa.core.domain.model.DtoArtistPagingItem
import com.poulastaa.core.domain.repository.ArtistId
import com.poulastaa.core.domain.repository.search.LocalArtistPagingDatasource
import com.poulastaa.search.repository.ArtistPagingRepository

internal class ArtistPagingRepositoryService(
    private val db: LocalArtistPagingDatasource,
) : ArtistPagingRepository {
    override suspend fun getPagingSong(
        page: Int,
        size: Int,
        query: String?,
        artistId: ArtistId,
    ): List<DtoArtistPagingItem> = db.getPagingSong(page, size, query, artistId)

    override suspend fun getPagingAlbum(
        page: Int,
        size: Int,
        query: String?,
        artistId: ArtistId,
    ): List<DtoArtistPagingItem> = db.getPagingAlbum(page, size, query, artistId)
}
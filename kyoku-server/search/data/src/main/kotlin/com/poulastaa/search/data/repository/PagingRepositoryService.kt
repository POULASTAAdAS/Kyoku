package com.poulastaa.search.data.repository

import com.poulastaa.core.domain.model.DtoExploreAlbumFilterType
import com.poulastaa.core.domain.model.DtoSearchItem
import com.poulastaa.core.domain.repository.ArtistId
import com.poulastaa.core.domain.repository.search.LocalPagingDatasource
import com.poulastaa.search.repository.PagingRepository

internal class PagingRepositoryService(
    private val db: LocalPagingDatasource,
) : PagingRepository {
    override suspend fun getArtistPagingSong(
        page: Int,
        size: Int,
        query: String?,
        artistId: ArtistId,
    ): List<DtoSearchItem> = db.getArtistPagingSong(page, size, query, artistId)

    override suspend fun getArtistPagingAlbum(
        page: Int,
        size: Int,
        query: String?,
        artistId: ArtistId,
    ): List<DtoSearchItem> = db.getArtistPagingAlbum(page, size, query, artistId)

    override suspend fun getPagingAlbum(
        query: String?,
        page: Int,
        size: Int,
        filterType: DtoExploreAlbumFilterType,
    ): List<DtoSearchItem> = db.getPagingAlbum(query, page, size, filterType)
}
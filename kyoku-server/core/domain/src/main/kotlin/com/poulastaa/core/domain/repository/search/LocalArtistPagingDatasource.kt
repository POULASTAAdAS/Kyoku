package com.poulastaa.core.domain.repository.search

import com.poulastaa.core.domain.model.DtoArtistPagingItem
import com.poulastaa.core.domain.repository.ArtistId

interface LocalArtistPagingDatasource {
    suspend fun getPagingSong(
        page: Int,
        size: Int,
        query: String?,
        artistId: ArtistId,
    ): List<DtoArtistPagingItem>

    suspend fun getPagingAlbum(
        page: Int,
        size: Int,
        query: String?,
        artistId: ArtistId,
    ): List<DtoArtistPagingItem>
}
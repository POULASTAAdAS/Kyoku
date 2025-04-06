package com.poulastaa.core.domain.repository.search

import com.poulastaa.core.domain.model.DtoExploreAlbumFilterType
import com.poulastaa.core.domain.model.DtoExploreArtistFilterType
import com.poulastaa.core.domain.model.DtoSearchItem
import com.poulastaa.core.domain.repository.ArtistId

interface LocalPagingDatasource {
    suspend fun getArtistPagingSong(
        page: Int,
        size: Int,
        query: String?,
        artistId: ArtistId,
    ): List<DtoSearchItem>

    suspend fun getArtistPagingAlbum(
        page: Int,
        size: Int,
        query: String?,
        artistId: ArtistId,
    ): List<DtoSearchItem>

    suspend fun getPagingAlbum(
        query: String?,
        page: Int,
        size: Int,
        filterType: DtoExploreAlbumFilterType,
    ): List<DtoSearchItem>

    suspend fun getPagingArtist(
        query: String?,
        page: Int,
        size: Int,
        filterType: DtoExploreArtistFilterType,
    ): List<DtoSearchItem>
}
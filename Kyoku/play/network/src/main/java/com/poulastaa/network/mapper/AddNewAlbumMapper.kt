package com.poulastaa.network.mapper

import com.poulastaa.core.domain.model.AlbumPagingType
import com.poulastaa.core.domain.model.PagingAlbumData
import com.poulastaa.network.model.AlbumPagingTypeDto
import com.poulastaa.network.model.PagingAlbumDto

fun PagingAlbumDto.toPagingAlbumData() = PagingAlbumData(
    id = this.id,
    name = this.name,
    coverImage = this.coverImage,
    releaseYear = this.releaseYear,
    artist = this.artist
)

fun AlbumPagingType.toAlbumPagingTypeDto() = when (this) {
    AlbumPagingType.NAME -> AlbumPagingTypeDto.NAME
    AlbumPagingType.BY_YEAR -> AlbumPagingTypeDto.BY_YEAR
    AlbumPagingType.BY_POPULARITY -> AlbumPagingTypeDto.BY_POPULARITY
}
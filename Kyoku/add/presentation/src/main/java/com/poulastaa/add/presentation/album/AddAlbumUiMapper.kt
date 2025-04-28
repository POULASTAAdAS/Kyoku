package com.poulastaa.add.presentation.album

import com.poulastaa.add.domain.model.DtoAddAlbum
import com.poulastaa.add.domain.model.DtoAddAlbumSearchFilterType

internal fun AddAlbumSearchUiFilterType.toDtoAddAlbumFilterType() = when (this) {
    AddAlbumSearchUiFilterType.MOST_POPULAR -> DtoAddAlbumSearchFilterType.POPULARITY
    AddAlbumSearchUiFilterType.RELEASE_YEAR -> DtoAddAlbumSearchFilterType.RELEASE_YEAR
}

internal fun DtoAddAlbum.toUiAlbum() = UiAlbum(
    id = this.id,
    name = this.title,
    poster = this.poster ?: "",
    artist = this.artist,
    releaseYear = this.releaseYear,
    isSelected = this.isSelected
)
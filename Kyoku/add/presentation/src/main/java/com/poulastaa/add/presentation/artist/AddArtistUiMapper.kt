package com.poulastaa.add.presentation.artist

import com.poulastaa.add.domain.model.DtoAddArtist
import com.poulastaa.add.domain.model.DtoAddArtistFilterType

internal fun AddArtistSearchUiFilterType.toDtoAddArtistFilterType() = when (this) {
    AddArtistSearchUiFilterType.ALL -> DtoAddArtistFilterType.ALL
    AddArtistSearchUiFilterType.INTERNATIONAL -> DtoAddArtistFilterType.INTERNATIONAL
}

internal fun DtoAddArtist.toUiArtist() = UiArtist(
    id = this.id,
    name = this.title,
    poster = this.poster ?: "",
    isSelected = false,
)
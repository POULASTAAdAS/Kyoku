package com.poulastaa.setup.presentation.pic_artist

import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.DtoPrevArtist

internal fun DtoPrevArtist.toUiArtist(list: List<ArtistId>) = UiArtist(
    id = this.id,
    name = this.name,
    cover = this.cover,
    isSelected = list.contains(this.id)
)
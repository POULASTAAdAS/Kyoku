package com.poulastaa.view.data.mapper

import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.view.domain.model.DtoViewArtist

internal fun DtoPrevArtist.toDtoViewArtist(isFollowing: Boolean) = DtoViewArtist(
    id = this.id,
    name = this.name,
    cover = this.cover,
    isFollowing = isFollowing
)
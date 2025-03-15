package com.poulastaa.view.presentation.artist.mapper

import com.poulastaa.view.domain.model.DtoViewArtist
import com.poulastaa.view.presentation.artist.UiViewArtist

internal fun DtoViewArtist.toUiViewArtist() = UiViewArtist(
    id = this.id,
    name = this.name,
    cover = this.cover ?: "",
    isFollowing = this.isFollowing
)
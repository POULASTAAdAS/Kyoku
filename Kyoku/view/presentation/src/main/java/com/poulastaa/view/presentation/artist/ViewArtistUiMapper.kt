package com.poulastaa.view.presentation.artist

import com.poulastaa.view.domain.model.DtoViewArtist

internal fun DtoViewArtist.toUiViewArtist() = UiViewArtist(
    id = this.id,
    name = this.name,
    cover = this.cover,
    isFollowing = this.isFollowing
)
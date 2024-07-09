package com.poulastaa.setup.presentation.set_artist.mapper

import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.presentation.ui.model.UiArtist

fun Artist.toUiArtist() = UiArtist(
    id = id,
    name = name,
    coverImageUrl = coverImage ?: "",
    isSelected = false
)

fun UiArtist.toArtist() = Artist(
    id = id,
    name = name,
    coverImage = coverImageUrl
)
package com.poulastaa.play.presentation.root_drawer.library

import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.presentation.ui.model.UiArtist

fun Artist.toUiArtist() = UiArtist(
    id = id,
    name = name,
    coverImageUrl = coverImage ?: ""
)
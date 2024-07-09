package com.poulastaa.setup.presentation.set_artist.mapper

import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.presentation.ui.model.SpotifyUiArtist

fun Artist.toUiArtist() = SpotifyUiArtist(
    id = id,
    name = name,
    coverImageUrl = coverImage ?: "",
    isSelected = false
)

fun SpotifyUiArtist.toArtist() = Artist(
    id = id,
    name = name,
    coverImage = coverImageUrl
)
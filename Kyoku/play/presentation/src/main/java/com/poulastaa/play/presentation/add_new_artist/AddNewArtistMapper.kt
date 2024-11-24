package com.poulastaa.play.presentation.add_new_artist

import com.poulastaa.core.domain.model.Artist

fun Artist.toPagingArtist() = AddArtistUiArtist(
    id = this.id,
    name = this.name,
    coverImage = this.coverImage ?: ""
)
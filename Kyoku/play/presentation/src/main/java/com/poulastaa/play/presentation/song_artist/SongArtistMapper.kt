package com.poulastaa.play.presentation.song_artist

import com.poulastaa.core.domain.model.ArtistWithPopularity

fun ArtistWithPopularity.toSongArtistUiArtist() = SongArtistUiArtist(
    id = this.id,
    name = this.name,
    coverImage = this.coverImage,
    popularity = this.popularity,
)
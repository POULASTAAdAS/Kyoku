package com.poulastaa.play.presentation.explore_artist

import com.poulastaa.core.domain.model.ArtistSingleData

fun ArtistSingleData.toExploreArtistSingleUiData() = ExploreArtistSingleUiData(
    id = this.id,
    title = this.title,
    coverImage = this.coverImage,
    releaseYear = this.releaseYear,
    isExpanded = false
)
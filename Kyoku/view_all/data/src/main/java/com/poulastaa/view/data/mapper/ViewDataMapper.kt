package com.poulastaa.view.data.mapper

import com.poulastaa.core.domain.model.DtoDetailedPrevSong
import com.poulastaa.core.domain.model.DtoExploreType
import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.core.domain.model.DtoSong
import com.poulastaa.core.domain.model.ViewType
import com.poulastaa.view.domain.model.DtoViewArtist

internal fun DtoPrevArtist.toDtoViewArtist(isFollowing: Boolean) = DtoViewArtist(
    id = this.id,
    name = this.name,
    cover = this.cover,
    isFollowing = isFollowing
)

internal fun ViewType.toDtoExploreType() = when (this) {
    ViewType.POPULAR_SONG_MIX -> DtoExploreType.POPULAR_SONG_MIX
    ViewType.POPULAR_YEAR_MIX -> DtoExploreType.POPULAR_YEAR_MIX
    ViewType.SAVED_ARTIST_SONG_MIX -> DtoExploreType.POPULAR_ARTIST_SONG_MIX
    ViewType.DAY_TYPE_MIX -> DtoExploreType.DAY_TYPE_MIX
    else -> throw IllegalArgumentException("Invalid Explore Type")
}

internal fun DtoSong.toDtoDetailedPrevSong() = DtoDetailedPrevSong(
    id = this.id,
    title = this.title,
    poster = this.poster,
    artists = this.artist.joinToString(","),
    releaseYear = this.info.releaseYear
)
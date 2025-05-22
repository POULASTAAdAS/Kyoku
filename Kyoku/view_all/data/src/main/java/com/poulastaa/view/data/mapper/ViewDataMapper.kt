package com.poulastaa.view.data.mapper

import com.poulastaa.core.domain.model.DtoAlbum
import com.poulastaa.core.domain.model.DtoArtist
import com.poulastaa.core.domain.model.DtoDetailedPrevSong
import com.poulastaa.core.domain.model.DtoExploreType
import com.poulastaa.core.domain.model.DtoPlaylistPayload
import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.core.domain.model.DtoSong
import com.poulastaa.core.domain.model.ViewType
import com.poulastaa.view.domain.model.DtoViewArtist
import com.poulastaa.view.domain.model.DtoViewSavedItem

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

@JvmName("DtoArtistToDtoViewSavedItem")
internal fun DtoArtist.toDtoViewSavedItem() = DtoViewSavedItem(
    id = this.id,
    title = this.name,
    poster = listOf(this.coverImage ?: ""),
    numbers = this.popularity
)

@JvmName("DtoAlbumToDtoViewSavedItem")
internal fun Map.Entry<String?, DtoAlbum>.toDtoViewSavedItem() = DtoViewSavedItem(
    id = this.value.id,
    title = this.value.name,
    poster = listOf(this.value.poster ?: ""),
    numbers = this.value.popularity,
    artist = this.key
)

@JvmName("DtoPlaylistPayloadToDtoViewSavedItem")
internal fun DtoPlaylistPayload.toDtoViewSavedItem() = DtoViewSavedItem(
    id = this.playlist.id,
    title = this.playlist.name,
    poster = this.cover,
    numbers = this.count.toLong()
)
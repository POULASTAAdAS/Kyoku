package com.poulastaa.core.database.mapper

import com.poulastaa.core.database.entity.AlbumEntity
import com.poulastaa.core.database.entity.ArtistEntity
import com.poulastaa.core.domain.PinReqType
import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.domain.model.PinnedData
import com.poulastaa.core.domain.model.PinnedResult
import com.poulastaa.core.domain.model.PinnedType

fun ArtistEntity.toArtist() = Artist(
    id = id,
    name = name,
    coverImage = coverImage
)

fun Map.Entry<Long, List<PinnedResult>>.toPinnedData(pinnedType: PinnedType) = PinnedData(
    id = this.key,
    name = this.value.first().name,
    urls = this.value.map { it.coverImage },
    pinnedType = pinnedType
)

fun AlbumEntity.toPinnedData() = PinnedData(
    id = id,
    name = name,
    urls = listOf(coverImage),
    pinnedType = PinnedType.ALBUM
)

fun ArtistEntity.toPinnedData() = PinnedData(
    id = id,
    name = name,
    urls = listOf(coverImage),
    pinnedType = PinnedType.ARTIST
)

fun PinReqType.toPinnedType() = when (this) {
    PinReqType.PLAYLIST -> PinnedType.PLAYLIST
    PinReqType.ARTIST -> PinnedType.ARTIST
    PinReqType.ALBUM -> PinnedType.ALBUM
    PinReqType.FAVOURITE -> throw IllegalArgumentException("Favourite cannot be pinned")
}
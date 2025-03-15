package com.poulastaa.view.network.mapper

import com.poulastaa.core.domain.model.DtoDetailedPrevSong
import com.poulastaa.core.network.mapper.toPrevArtistRes
import com.poulastaa.core.domain.model.DtoHeading
import com.poulastaa.view.domain.model.DtoViewArtistPayload
import com.poulastaa.core.domain.model.DtoViewOtherPayload
import com.poulastaa.core.domain.model.DtoViewType
import com.poulastaa.view.network.model.*

private fun DtoDetailedPrevSong.toResponseDetailPrevSong() = ResponseDetailPrevSong(
    id = this.id,
    title = this.title,
    poster = this.poster,
    artists = this.artists,
    releaseYear = this.releaseYear
)

internal fun DtoViewArtistPayload.toResponseViewArtist() = ViewArtistResponse(
    artist = this.artist.toPrevArtistRes(),
    songs = this.songs.map { it.toResponseDetailPrevSong() }
)

internal fun ViewTypeReq.toDtoViewType() = when (this) {
    ViewTypeReq.PLAYLIST -> DtoViewType.PLAYLIST
    ViewTypeReq.ALBUM -> DtoViewType.ALBUM
    ViewTypeReq.FAVOURITE -> DtoViewType.FAVOURITE
    ViewTypeReq.POPULAR_SONG_MIX -> DtoViewType.POPULAR_SONG_MIX
    ViewTypeReq.POPULAR_YEAR_MIX -> DtoViewType.POPULAR_YEAR_MIX
    ViewTypeReq.SAVED_ARTIST_SONG_MIX -> DtoViewType.SAVED_ARTIST_SONG_MIX
    ViewTypeReq.DAY_TYPE_MIX -> DtoViewType.DAY_TYPE_MIX
}

private fun DtoHeading.toHeadingResponse() = HeadingResponse(
    type = when (this.type) {
        DtoViewType.PLAYLIST -> ViewTypeResponse.PLAYLIST
        DtoViewType.ALBUM -> ViewTypeResponse.ALBUM
        DtoViewType.FAVOURITE -> ViewTypeResponse.FAVOURITE
        DtoViewType.POPULAR_SONG_MIX -> ViewTypeResponse.POPULAR_SONG_MIX
        DtoViewType.POPULAR_YEAR_MIX -> ViewTypeResponse.POPULAR_YEAR_MIX
        DtoViewType.SAVED_ARTIST_SONG_MIX -> ViewTypeResponse.SAVED_ARTIST_SONG_MIX
        DtoViewType.DAY_TYPE_MIX -> ViewTypeResponse.DAY_TYPE_MIX
    },
    id = this.id,
    name = this.name,
    poster = this.poster,
)

internal fun DtoViewOtherPayload.toViewResponse() = ViewOtherResponse(
    heading = this.heading.toHeadingResponse(),
    songs = this.songs.map { it.toResponseDetailPrevSong() }
)
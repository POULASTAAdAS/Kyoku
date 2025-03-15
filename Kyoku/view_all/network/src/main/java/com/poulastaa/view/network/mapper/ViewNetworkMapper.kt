package com.poulastaa.view.network.mapper

import com.poulastaa.core.domain.model.DtoDetailedPrevSong
import com.poulastaa.core.domain.model.DtoHeading
import com.poulastaa.core.domain.model.DtoViewPayload
import com.poulastaa.core.domain.model.ViewType
import com.poulastaa.core.network.mapper.toDtoPrevArtist
import com.poulastaa.core.network.mapper.toDtoSong
import com.poulastaa.core.network.model.ResponseSong
import com.poulastaa.view.domain.model.DtoViewArtisPayload
import com.poulastaa.view.network.model.HeadingResponse
import com.poulastaa.view.network.model.ResponseDetailPrevSong
import com.poulastaa.view.network.model.ViewArtistResponse
import com.poulastaa.view.network.model.ViewOtherResponse
import com.poulastaa.view.network.model.ViewTypeReq
import com.poulastaa.view.network.model.ViewTypeResponse

private fun ResponseDetailPrevSong.toDtoDetailedPrevSong() = DtoDetailedPrevSong(
    id = this.id,
    title = this.title,
    artists = this.artists,
    poster = this.poster
)

internal fun ViewArtistResponse.toDtoViewArtist() = DtoViewArtisPayload(
    artist = this.artist.toDtoPrevArtist(),
    mostPopularSongs = this.songs.map { it.toDtoDetailedPrevSong() }
)

internal fun ViewType.toExploreTypeReq() = when (this) {
    ViewType.POPULAR_SONG_MIX -> ViewTypeReq.POPULAR_SONG_MIX
    ViewType.POPULAR_YEAR_MIX -> ViewTypeReq.POPULAR_YEAR_MIX
    ViewType.SAVED_ARTIST_SONG_MIX -> ViewTypeReq.SAVED_ARTIST_SONG_MIX
    ViewType.DAY_TYPE_MIX -> ViewTypeReq.DAY_TYPE_MIX
    else -> throw IllegalArgumentException("Invalid ViewType")
}

private fun ViewTypeResponse.toViewType() = when (this) {
    ViewTypeResponse.PLAYLIST -> ViewType.PLAYLIST
    ViewTypeResponse.ALBUM -> ViewType.ALBUM
    ViewTypeResponse.FAVOURITE -> ViewType.FAVOURITE
    ViewTypeResponse.POPULAR_SONG_MIX -> ViewType.POPULAR_SONG_MIX
    ViewTypeResponse.POPULAR_YEAR_MIX -> ViewType.POPULAR_YEAR_MIX
    ViewTypeResponse.SAVED_ARTIST_SONG_MIX -> ViewType.SAVED_ARTIST_SONG_MIX
    ViewTypeResponse.DAY_TYPE_MIX -> ViewType.DAY_TYPE_MIX
}

private fun HeadingResponse.toDtoHeading() = DtoHeading(
    type = this.type.toViewType(),
    id = this.id,
    name = this.name,
    poster = this.poster
)

@JvmName("toDtoViewOtherResponseSong")
internal fun ViewOtherResponse<ResponseSong>.toDtoViewOtherResponse() = DtoViewPayload(
    heading = this.heading.toDtoHeading(),
    listOfSongs = this.songs.map { it.toDtoSong() }
)

@JvmName("toDtoViewOtherResponseDetailPrevSong")
internal fun ViewOtherResponse<ResponseDetailPrevSong>.toDtoViewOtherResponse() = DtoViewPayload(
    heading = this.heading.toDtoHeading(),
    listOfSongs = this.songs.map { it.toDtoDetailedPrevSong() }
)
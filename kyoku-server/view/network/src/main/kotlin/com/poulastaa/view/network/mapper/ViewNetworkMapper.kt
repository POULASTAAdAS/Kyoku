package com.poulastaa.view.network.mapper

import com.poulastaa.core.network.mapper.toPrevArtistRes
import com.poulastaa.core.domain.model.DtoDetailedPrevSong
import com.poulastaa.view.domain.model.ViewArtistPayload
import com.poulastaa.view.network.model.ResponseDetailPrevSong
import com.poulastaa.view.network.model.ViewArtistResponse

private fun DtoDetailedPrevSong.toResponseDetailPrevSong() = ResponseDetailPrevSong(
    id = this.id,
    title = this.title,
    poster = this.poster,
    artists = this.artists,
    releaseYear = this.releaseYear
)

internal fun ViewArtistPayload.toResponseViewArtist() = ViewArtistResponse(
    artist = this.artist.toPrevArtistRes(),
    songs = this.songs.map { it.toResponseDetailPrevSong() }
)
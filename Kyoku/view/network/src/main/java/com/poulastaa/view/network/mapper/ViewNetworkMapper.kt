package com.poulastaa.view.network.mapper

import com.poulastaa.core.domain.model.DtoDetailedPrevSong
import com.poulastaa.core.network.mapper.toDtoPrevArtist
import com.poulastaa.view.domain.model.DtoViewArtisPayload
import com.poulastaa.view.network.model.ResponseDetailPrevSong
import com.poulastaa.view.network.model.ViewArtistResponse

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
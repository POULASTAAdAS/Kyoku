package com.poulastaa.view.network.mapper

import com.poulastaa.core.network.mapper.toPrevArtistRes
import com.poulastaa.core.network.mapper.toResponsePrevSong
import com.poulastaa.view.domain.model.ViewArtistPayload
import com.poulastaa.view.network.model.ViewArtistResponse

internal fun ViewArtistPayload.toResponseViewArtist() = ViewArtistResponse(
    artist = this.artist.toPrevArtistRes(),
    songs = this.songs.map { it.toResponsePrevSong() }
)
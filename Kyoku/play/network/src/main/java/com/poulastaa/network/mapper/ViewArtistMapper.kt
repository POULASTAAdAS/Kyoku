package com.poulastaa.network.mapper

import com.poulastaa.core.domain.model.ViewArtistData
import com.poulastaa.core.domain.model.ViewArtistSong
import com.poulastaa.network.model.ViewArtistDto
import com.poulastaa.network.model.ViewArtistSongDto

fun ViewArtistSongDto.toViewArtistSong() = ViewArtistSong(
    id = this.id,
    title = this.title,
    coverImage = this.coverImage,
    popularity = this.popularity
)

fun ViewArtistDto.toViewArtistData() = ViewArtistData(
    followers = this.followers,
    artist = this.artist.toArtist(),
    listOfSong = this.listOfSong.map { it.toViewArtistSong() }
)
package com.poulastaa.core.network.mapper

import com.poulastaa.core.domain.model.DtoAlbum
import com.poulastaa.core.domain.model.DtoArtist
import com.poulastaa.core.domain.model.DtoCountry
import com.poulastaa.core.domain.model.DtoGenre
import com.poulastaa.core.domain.model.DtoPlaylist
import com.poulastaa.core.domain.model.DtoSong
import com.poulastaa.core.domain.model.DtoSongInfo
import com.poulastaa.core.network.model.ResponseAlbum
import com.poulastaa.core.network.model.ResponseArtist
import com.poulastaa.core.network.model.ResponseCountry
import com.poulastaa.core.network.model.ResponseGenre
import com.poulastaa.core.network.model.ResponsePlaylist
import com.poulastaa.core.network.model.ResponseSong
import com.poulastaa.core.network.model.ResponseSongInfo

fun ResponsePlaylist.toDtoPlaylist() = DtoPlaylist(
    id = this.id,
    name = this.name,
    popularity = this.popularity,
    visibilityState = this.visibilityState
)

fun ResponseGenre.toDtoGenre() = DtoGenre(
    id = this.id,
    name = this.name
)

fun ResponseCountry.toDtoCountry() = DtoCountry(
    id = this.id,
    name = this.name
)

fun ResponseAlbum.toDtoAlbum() = DtoAlbum(
    id = this.id,
    name = this.name,
    poster = this.poster,
    popularity = this.popularity
)

fun ResponseArtist.toDtoArtist() = DtoArtist(
    id = this.id,
    name = this.name,
    coverImage = this.coverImage,
    popularity = this.popularity,
    genre = this.genre?.toDtoGenre(),
    country = this.country?.toDtoCountry()
)

fun ResponseSongInfo.toDtoSongInfo() = DtoSongInfo(
    songId = this.id,
    releaseYear = this.releaseYear,
    composer = this.composer,
    popularity = this.popularity
)

fun ResponseSong.toDtoSong() = DtoSong(
    id = this.id,
    title = this.title,
    poster = this.poster,
    masterPlaylist = this.masterPlaylist,
    artist = this.artist.map { it.toDtoArtist() },
    album = this.album?.toDtoAlbum(),
    info = this.info.toDtoSongInfo(),
    genre = this.genre?.toDtoGenre()
)
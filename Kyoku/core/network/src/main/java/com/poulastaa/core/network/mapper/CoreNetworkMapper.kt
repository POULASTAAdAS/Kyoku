package com.poulastaa.core.network.mapper

import com.poulastaa.core.domain.model.DtoAlbum
import com.poulastaa.core.domain.model.DtoArtist
import com.poulastaa.core.domain.model.DtoCountry
import com.poulastaa.core.domain.model.DtoFullPlaylist
import com.poulastaa.core.domain.model.DtoGenre
import com.poulastaa.core.domain.model.DtoPlaylist
import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.core.domain.model.DtoPrevSong
import com.poulastaa.core.domain.model.DtoSong
import com.poulastaa.core.domain.model.DtoSongInfo
import com.poulastaa.core.network.model.ResponseAlbum
import com.poulastaa.core.network.model.ResponseArtist
import com.poulastaa.core.network.model.ResponseCountry
import com.poulastaa.core.network.model.ResponseFullPlaylist
import com.poulastaa.core.network.model.ResponseGenre
import com.poulastaa.core.network.model.ResponsePlaylist
import com.poulastaa.core.network.model.ResponsePrevArtist
import com.poulastaa.core.network.model.ResponsePrevSong
import com.poulastaa.core.network.model.ResponseSong
import com.poulastaa.core.network.model.ResponseSongInfo

fun ResponsePlaylist.toDtoPlaylist() = DtoPlaylist(
    id = this.id,
    name = this.name,
    popularity = this.popularity,
    visibilityState = this.visibilityState
)

fun ResponseFullPlaylist.toDtoFullPlaylist() = DtoFullPlaylist(
    playlist = this.playlist.toDtoPlaylist(),
    songs = this.listOfSong.map { it.toDtoSong() }
)

fun ResponseGenre.toDtoGenre() = DtoGenre(
    id = this.id,
    name = this.name,
    cover = this.cover
)

fun ResponsePrevArtist.toDtoPrevArtist() = DtoPrevArtist(
    id = this.id,
    name = this.name,
    cover = this.cover
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
)

fun ResponsePrevSong.toDtoPrevSong() = DtoPrevSong(
    id = this.id,
    title = this.title,
    poster = this.poster
)

fun ResponseArtist.toDtoPrevArtist() = DtoPrevArtist(
    id = this.id,
    name = this.name,
    cover = this.coverImage
)
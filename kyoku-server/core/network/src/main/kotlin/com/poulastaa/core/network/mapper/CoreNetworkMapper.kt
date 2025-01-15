package com.poulastaa.core.network.mapper

import com.poulastaa.core.domain.model.*
import com.poulastaa.core.network.model.*

fun DtoGenre.toResponseGenre() = ResponseGenre(
    id = this.id,
    name = this.name
)

fun DtoCountry.toResponseCountry() = ResponseCountry(
    id = this.id,
    name = this.name
)

fun DtoAlbum.toResponseAlbum() = ResponseAlbum(
    id = this.id,
    name = this.name,
    poster = this.poster,
    popularity = this.popularity
)

fun DtoArtist.toResponseArtist() = ResponseArtist(
    id = this.id,
    name = this.name,
    coverImage = this.coverImage,
    popularity = this.popularity,
    genre = this.genre?.toResponseGenre(),
    country = this.country?.toResponseCountry()
)

fun DtoSongInfo.toResponseSongInfo() = ResponseSongInfo(
    id = this.id,
    releaseYear = this.releaseYear,
    composer = this.composer,
    popularity = this.popularity
)

fun DtoPlaylist.toResponsePlaylist() = ResponsePlaylist(
    id = this.id,
    name = this.name,
    popularity = this.popularity,
    visibilityState = this.visibilityState
)

fun DtoSong.toResponseSong() = ResponseSong(
    id = this.id,
    title = this.title,
    poster = this.poster,
    masterPlaylist = this.masterPlaylist,
    artist = this.artist.map { it.toResponseArtist() },
    album = this.album?.toResponseAlbum(),
    info = this.info.toResponseSongInfo(),
    genre = this.genre?.toResponseGenre(),
)

fun DtoPlaylistFull.toResponsePlaylistFull() = ResponseFullPlaylist(
    playlist = this.playlist.toResponsePlaylist(),
    listOfSong = this.listOfSong.map { it.toResponseSong() }
)
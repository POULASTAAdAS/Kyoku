package com.poulastaa.core.network.mapper

import com.poulastaa.core.domain.model.*
import com.poulastaa.core.network.model.*

fun GenreDto.toResponseGenre() = ResponseGenre(
    id = this.id,
    name = this.name
)

fun CountryDto.toResponseCountry() = ResponseCountry(
    id = this.id,
    name = this.name
)

fun AlbumDto.toResponseAlbum() = ResponseAlbum(
    id = this.id,
    name = this.name,
    poster = this.poster
)

fun ArtistDto.toResponseArtist() = ResponseArtist(
    id = this.id,
    name = this.name,
    coverImage = this.coverImage,
    popularity = this.popularity,
    genre = this.genre.toResponseGenre(),
    country = this.country.toResponseCountry()
)

fun SongInfoDto.toResponseSongInfo() = ResponseSongInfo(
    id = this.id,
    releaseYear = this.releaseYear,
    composer = this.composer,
    popularity = this.popularity
)

fun PlaylistDto.toResponsePlaylist() = ResponsePlaylist(
    id = this.id,
    name = this.name,
    popularity = this.popularity
)

fun SongDto.toResponseSong() = ResponseSong(
    id = this.id,
    title = this.title,
    poster = this.poster,
    masterPlaylist = this.masterPlaylist,
    artist = this.artist.map { it.toResponseArtist() },
    album = this.album.toResponseAlbum(),
    info = this.info.toResponseSongInfo(),
    genre = this.genre?.toResponseGenre(),
)

fun PlaylistFullDto.toResponsePlaylistFull() = ResponseFullPlaylist(
    playlist = this.playlist.toResponsePlaylist(),
    listOfSong = this.listOfSong.map { it.toResponseSong() }
)
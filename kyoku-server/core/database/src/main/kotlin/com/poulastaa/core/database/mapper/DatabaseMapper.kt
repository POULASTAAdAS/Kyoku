package com.poulastaa.core.database.mapper

import com.poulastaa.core.database.dao.*
import com.poulastaa.core.domain.model.*

fun DaoUser.toDbUserDto() = DtoDBUser(
    id = id.value,
    email = email,
    userName = username,
    passwordHash = passwordHash,
    profilePicUrl = profilePicUrl,
    countryCode = countryId,
    bDate = bDate
)

fun DaoSong.toSongDto(
    artist: List<DtoArtist>,
    album: DtoAlbum?,
    info: DtoSongInfo,
    genre: DtoGenre?,
) = DtoSong(
    id = this.id.value,
    title = this.title,
    poster = this.poster,
    masterPlaylist = this.masterPlaylist,
    artist = artist,
    album = album,
    info = info,
    genre = genre
)

fun DaoPlaylist.toPlaylistDto() = DtoPlaylist(
    id = this.id.value,
    name = this.name,
    popularity = this.popularity
)

fun DaoArtist.toDbArtistDto() = DtoDBArtist(
    id = this.id.value,
    name = this.name,
    coverImage = this.coverImage,
    popularity = this.popularity
)

fun DtoDBArtist.toArtistDto(
    genre: DtoGenre,
    country: DtoCountry,
) = DtoArtist(
    id = this.id,
    name = this.name,
    coverImage = this.coverImage,
    popularity = this.popularity,
    genre = genre,
    country = country
)

fun DaoSongInfo.toSongInfoDto() = DtoSongInfo(
    id = this.id.value,
    releaseYear = this.releaseYear,
    composer = this.composer,
    popularity = this.popularity
)

fun DaoGenre.toGenreDto() = DtoGenre(
    id = this.id.value,
    name = this.genre,
    popularity = this.popularity
)


fun DaoAlbum.toAlbumDto() = DtoAlbum(
    id = this.id.value,
    name = this.name,
    popularity = this.popularity
)
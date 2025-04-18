package com.poulastaa.core.database.mapper

import com.poulastaa.core.database.dao.*
import com.poulastaa.core.domain.model.*
import java.time.LocalDate

fun DaoUser.toDbUserDto(bDate: LocalDate? = null) = DtoDBUser(
    id = this.id.value,
    email = this.email,
    userName = this.username,
    passwordHash = this.passwordHash,
    profilePicUrl = this.profilePicUrl,
    countryId = this.countryId,
    bDate = bDate ?: this.bDate
)

fun DaoSong.toSongDto(
    artist: List<DtoArtist>,
    album: DtoAlbum?,
    info: DtoSongInfo,
    genre: DtoGenre?,
) = DtoSong(
    id = this.id.value,
    title = this.title,
    rawPoster = this.poster,
    masterPlaylist = this.masterPlaylist,
    artist = artist,
    album = album,
    info = info,
    genre = genre
)

fun DaoPlaylist.toDtoPlaylist() = DtoPlaylist(
    id = this.id.value,
    name = this.name,
    popularity = this.popularity,
    visibilityState = this.isPublic
)

fun DaoArtist.toDbArtistDto() = DtoDBArtist(
    id = this.id.value,
    name = this.name,
    coverImage = this.coverImage,
    popularity = this.popularity
)

fun DtoDBArtist.toArtistDto(
    genre: DtoGenre?,
    country: DtoCountry?,
) = DtoArtist(
    id = this.id,
    name = this.name,
    rawCoverImage = this.coverImage,
    popularity = this.popularity,
    genre = genre,
    country = country
)

fun DaoGenre.toGenreDto() = DtoGenre(
    id = this.id.value,
    name = this.genre,
    rawCover = this.cover,
    popularity = this.popularity
)

fun DaoAlbum.toDtoAlbum() = DtoAlbum(
    id = this.id.value,
    name = this.name,
    popularity = this.popularity
)

fun DaoArtist.toDtoPrevArtist() = DtoPrevArtist(
    id = this.id.value,
    name = this.name,
    rawCover = this.coverImage
)

fun DtoArtist.toDtoPrevArtist() = DtoPrevArtist(
    id = this.id,
    name = this.name,
    rawCover = this.coverImage
)

internal fun DtoGenre.toDtoPrevGenre() = DtoPrevGenre(
    id = this.id,
    name = this.name
)

internal fun DtoSong.toDtoPrevSong() = DtoPrevSong(
    id = this.id,
    title = this.title,
    rawPoster = this.poster
)

fun DaoSong.toDtoPrevSong() = DtoPrevSong(
    id = this.id.value,
    title = this.title,
    rawPoster = this.poster
)

fun DtoPrevSong.toDetailedPrevSong(artist: String?, releaseYear: Int) = DtoDetailedPrevSong(
    id = this.id,
    title = this.title,
    rawPoster = this.poster,
    artists = artist,
    releaseYear = releaseYear,
)

fun DtoSong.toDtoDetailedPrevSong() = DtoDetailedPrevSong(
    id = this.id,
    title = this.title,
    rawPoster = this.poster,
    artists = this.artist.joinToString(",") { it.name },
    releaseYear = -1
)

internal fun DtoPlaylist.toDtoHeading() = DtoHeading(
    type = DtoViewType.PLAYLIST,
    id = this.id,
    name = this.name,
)

internal fun DtoAlbum.toDtoHeading() = DtoHeading(
    type = DtoViewType.ALBUM,
    id = this.id,
    name = this.name,
    poster = this.poster ?: ""
)
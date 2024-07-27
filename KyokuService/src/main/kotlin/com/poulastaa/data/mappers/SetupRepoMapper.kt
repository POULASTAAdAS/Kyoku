package com.poulastaa.data.mappers

import com.poulastaa.data.dao.ArtistDao
import com.poulastaa.data.dao.GenreDao
import com.poulastaa.data.dao.SongDao
import com.poulastaa.data.model.ArtistDto
import com.poulastaa.data.model.GenreDto
import com.poulastaa.domain.model.ResultSong
import com.poulastaa.domain.model.UserType

fun SongDao.toResultSong() = ResultSong(
    id = this.id.value,
    coverImage = this.constructCoverImage(),
    title = this.title,
    releaseYear = this.year,
    masterPlaylistUrl = this.constructMasterPlaylistUrl()
)


fun GenreDao.toGenreDto() = GenreDto(
    id = this.id.value,
    name = this.name
)

fun ArtistDao.toArtistDto() = ArtistDto(
    id = this.id.value,
    name = this.name,
    coverImage = this.constructProfilePic()
)

fun String.getUserType() = when (this) {
    UserType.EMAIL_USER.name -> UserType.EMAIL_USER
    UserType.GOOGLE_USER.name -> UserType.GOOGLE_USER
    else -> null
}
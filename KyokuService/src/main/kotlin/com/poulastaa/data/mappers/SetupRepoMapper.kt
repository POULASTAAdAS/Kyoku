package com.poulastaa.data.mappers

import com.poulastaa.data.dao.GenreDao
import com.poulastaa.data.dao.SongDao
import com.poulastaa.data.model.GenreDto
import com.poulastaa.domain.model.ResultSong

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
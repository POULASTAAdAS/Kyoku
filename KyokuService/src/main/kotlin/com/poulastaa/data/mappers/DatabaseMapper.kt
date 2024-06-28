package com.poulastaa.data.mappers

import com.poulastaa.data.dao.ArtistDao
import com.poulastaa.data.dao.SongDao
import com.poulastaa.domain.model.ResultArtist
import com.poulastaa.domain.model.ResultSong

fun ArtistDao.toArtistResult() = ResultArtist(
    id = this.id.value,
    name = this.name,
    profilePic = this.profilePic ?: ""
)

fun SongDao.toResultSong() = ResultSong(
    id = this.id.value,
    coverImage = this.constructCoverImage(),
    title = this.title,
    releaseYear = this.year,
    masterPlaylistUrl = this.constructMasterPlaylistUrl()
)
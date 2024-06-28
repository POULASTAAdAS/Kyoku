package com.poulastaa.data.mappers

import com.poulastaa.data.dao.SongDao
import com.poulastaa.data.model.SongDto

fun SongDao.toSonDto(artist: String) = SongDto(
    id = this.id.value,
    title = this.title,
    coverImage = this.constructCoverImage(),
    artistName = artist,
    releaseYear = this.year,
    masterPlaylistUrl = this.constructMasterPlaylistUrl()
)
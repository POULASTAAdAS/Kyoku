package com.poulastaa.data.mappers

import com.poulastaa.data.dao.ArtistDao
import com.poulastaa.data.dao.SongDao
import com.poulastaa.data.model.SongDto
import com.poulastaa.domain.model.ResultArtist

fun ArtistDao.toArtistResult() = ResultArtist(
    id = this.id.value,
    name = this.name,
    profilePic = this.constructProfilePic() ?: ""
)

fun SongDao.toSongDto(artist: String) = SongDto(
    id = this.id.value,
    coverImage = this.constructCoverImage(),
    title = this.title,
    artistName = artist,
    releaseYear = this.year,
    masterPlaylistUrl = this.constructMasterPlaylistUrl()
)


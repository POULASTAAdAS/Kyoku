package com.poulastaa.data.mappers

import com.poulastaa.data.model.PlaylistDto
import com.poulastaa.data.model.SongDto
import com.poulastaa.domain.model.PlaylistResult

fun PlaylistResult.toPlaylistDto() = PlaylistDto(
    id = this.id,
    name = this.name,
    listOfSong = this.listOfSong.map {
        SongDto(
            id = it.resultSong.id,
            coverImage = it.resultSong.coverImage,
            title = it.resultSong.title,
            artistName = it.artistList.joinToString { artist -> artist.name },
            releaseYear = it.resultSong.releaseYear,
            masterPlaylistUrl = it.resultSong.masterPlaylistUrl
        )
    }
)
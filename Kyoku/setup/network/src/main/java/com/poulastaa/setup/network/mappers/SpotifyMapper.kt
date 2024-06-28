package com.poulastaa.setup.network.mappers

import com.poulastaa.core.domain.model.Playlist
import com.poulastaa.core.domain.model.PlaylistWithSong
import com.poulastaa.core.domain.model.Song
import com.poulastaa.setup.network.model.res.PlaylistDto
import com.poulastaa.setup.network.model.res.SongDto


fun SongDto.toSong() = Song(
    id = this.id,
    coverImage = this.coverImage,
    title = this.title,
    artistName = this.artistName,
    releaseYear = this.releaseYear,
    masterPlaylistUrl = this.masterPlaylistUrl
)

fun PlaylistDto.toPlaylistWithSong() = PlaylistWithSong(
    playlist = Playlist(
        id = this.id,
        name = this.name,
    ),
    songs = this.listOfSong.map { it.toSong() },
)
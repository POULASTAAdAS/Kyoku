package com.poulastaa.network.mapper

import com.poulastaa.core.data.model.PlaylistDto
import com.poulastaa.core.domain.model.PlaylistData
import com.poulastaa.core.domain.model.SongOtherData
import com.poulastaa.network.model.SongOtherDto

fun PlaylistDto.toPlaylistData() = PlaylistData(
    id = this.id,
    name = this.name,
    listOfSong = this.listOfSong.map { it.toSong() }
)

fun SongOtherDto.toSongOtherData() = SongOtherData(
    otherId = this.otherId,
    title = this.title,
    coverImage = this.coverImage,
    isPlaylist = this.isPlaylist
)
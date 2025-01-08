package com.poulastaa.setup.network

import com.poulastaa.core.domain.model.DtoFullPlaylist
import com.poulastaa.core.network.mapper.toDtoPlaylist
import com.poulastaa.core.network.mapper.toDtoSong
import com.poulastaa.setup.network.model.ResponseFullPlaylist

fun ResponseFullPlaylist.toDtoPlaylist() = DtoFullPlaylist(
    playlist = this.playlist.toDtoPlaylist(),
    songs = this.listOfSong.map { it.toDtoSong() }
)
package com.poulastaa.setup.network

import com.poulastaa.core.domain.model.DtoFullPlaylist
import com.poulastaa.core.network.mapper.toDtoGenre
import com.poulastaa.core.network.mapper.toDtoPlaylist
import com.poulastaa.core.network.mapper.toDtoSong
import com.poulastaa.setup.domain.model.SetBDateStatus
import com.poulastaa.setup.network.model.ResponseFullPlaylist
import com.poulastaa.setup.network.model.SetBDateStatusRes
import com.poulastaa.setup.network.model.SuggestedGenreRes

fun ResponseFullPlaylist.toDtoPlaylist() = DtoFullPlaylist(
    playlist = this.playlist.toDtoPlaylist(),
    songs = this.listOfSong.map { it.toDtoSong() }
)

fun SetBDateStatusRes.toSetBDateStatus() = when (this) {
    SetBDateStatusRes.SUCCESS -> SetBDateStatus.SUCCESS
    SetBDateStatusRes.FAILURE -> SetBDateStatus.FAILURE
}

fun SuggestedGenreRes.toDtoGenre() = this.list.map {
    it.toDtoGenre()
}
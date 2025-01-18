package com.poulastaa.setup.network

import com.poulastaa.core.domain.model.DtoFullPlaylist
import com.poulastaa.core.network.mapper.toDtoPlaylist
import com.poulastaa.core.network.mapper.toDtoSong
import com.poulastaa.setup.domain.model.SetBDateStatus
import com.poulastaa.setup.network.model.ResponseFullPlaylist
import com.poulastaa.setup.network.model.SetBDateStatusRes

fun ResponseFullPlaylist.toDtoPlaylist() = DtoFullPlaylist(
    playlist = this.playlist.toDtoPlaylist(),
    songs = this.listOfSong.map { it.toDtoSong() }
)

fun SetBDateStatusRes.toSetBDateStatus() = when (this) {
    SetBDateStatusRes.SUCCESS -> SetBDateStatus.SUCCESS
    SetBDateStatusRes.FAILURE -> SetBDateStatus.FAILURE
}

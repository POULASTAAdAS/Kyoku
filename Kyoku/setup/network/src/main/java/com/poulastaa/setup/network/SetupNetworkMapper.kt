package com.poulastaa.setup.network

import com.poulastaa.core.domain.model.DtoFullPlaylist
import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.core.network.mapper.toDtoGenre
import com.poulastaa.core.network.mapper.toDtoPlaylist
import com.poulastaa.core.network.mapper.toDtoSong
import com.poulastaa.core.network.model.ResponseFullPlaylist
import com.poulastaa.core.network.model.ResponsePrevArtist
import com.poulastaa.setup.domain.model.SetBDateStatus
import com.poulastaa.setup.network.model.SetBDateStatusRes
import com.poulastaa.setup.network.model.SuggestedArtistRes
import com.poulastaa.setup.network.model.SuggestedGenreRes

internal fun ResponseFullPlaylist.toDtoPlaylist() = DtoFullPlaylist(
    playlist = this.playlist.toDtoPlaylist(),
    songs = this.listOfSong.map { it.toDtoSong() }
)

internal fun SetBDateStatusRes.toSetBDateStatus() = when (this) {
    SetBDateStatusRes.SUCCESS -> SetBDateStatus.SUCCESS
    SetBDateStatusRes.FAILURE -> SetBDateStatus.FAILURE
}

internal fun SuggestedGenreRes.toDtoGenre() = this.list.map {
    it.toDtoGenre()
}

private fun ResponsePrevArtist.toDtoPrevArtist() = DtoPrevArtist(
    id = this.id,
    name = this.name,
    cover = this.cover
)

fun SuggestedArtistRes.toDtoPrevArtist() = this.list.map {
    it.toDtoPrevArtist()
}
package com.poulastaa.setup.presentation.spotify_playlist

import com.poulastaa.core.domain.model.DtoPlaylist
import com.poulastaa.core.domain.model.DtoPrevPlaylist
import com.poulastaa.core.domain.model.DtoDetailedPrevSong
import com.poulastaa.core.presentation.designsystem.model.UiPrevSong

fun DtoPlaylist.toUiPlaylist(totalSongs: Int) = UiPlaylist(
    id = this.id,
    name = this.name,
    totalSongs = totalSongs
)

fun DtoDetailedPrevSong.toUiPrevSong() = UiPrevSong(
    id = this.id,
    title = this.title,
    artists = this.artists,
    poster = this.poster
)


fun DtoPrevPlaylist.toUiPrevPlaylist() = UiPrevPlaylist(
    playlist = this.playlist.toUiPlaylist(this.list.size),
    songs = this.list.map { it.toUiPrevSong() }
)
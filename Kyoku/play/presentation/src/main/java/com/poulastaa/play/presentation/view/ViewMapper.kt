package com.poulastaa.play.presentation.view

import com.poulastaa.core.domain.model.PlaylistSong
import com.poulastaa.core.domain.model.ViewData
import com.poulastaa.core.presentation.ui.model.ViewUiSong

fun PlaylistSong.toViewUiData() = ViewUiSong(
    id = this.id,
    name = this.title,
    coverImage = this.coverImage,
    artist = this.artist
)

@JvmName("viewDataToViewUiData")
fun ViewData.toViewUiData() = ViewUiData(
    id = this.id,
    name = this.name,
    urls = this.listOfSong.shuffled().take(4).map { it.coverImage },
    listOfSong = this.listOfSong.map { it.toViewUiData() },
)

fun List<PlaylistSong>.toOtherData() = ViewUiData(
    urls = this.shuffled().take(4).map { it.coverImage },
    listOfSong = this.map { it.toViewUiData() }
)

@JvmName("playlistSongToViewUiData")
fun List<PlaylistSong>.toViewData() = ViewData(
    listOfSong = this
)
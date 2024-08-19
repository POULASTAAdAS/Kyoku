package com.poulastaa.play.presentation.view

import com.poulastaa.core.ViewData
import com.poulastaa.core.domain.model.PlaylistSong

fun PlaylistSong.toViewUiSong() = ViewUiSong(
    id = this.id,
    name = this.title,
    coverImage = this.coverImage,
    artist = this.artist
)

fun ViewData.toViewUiSong() = ViewUiData(
    id = this.id,
    name = this.name,
    urls = this.listOfSong.shuffled().take(4).map { it.coverImage },
    listOfSong = this.listOfSong.map { it.toViewUiSong() }
)

fun ViewData.toViewUiAlbum() = ViewUiData(
    id = this.id,
    name = this.name,
    urls = this.listOfSong.take(1).map { it.coverImage },
    listOfSong = this.listOfSong.map { it.toViewUiSong() }
)


fun List<PlaylistSong>.toOtherData() = ViewUiData(
    urls = this.shuffled().take(4).map { it.coverImage },
    listOfSong = this.map { it.toViewUiSong() }
)
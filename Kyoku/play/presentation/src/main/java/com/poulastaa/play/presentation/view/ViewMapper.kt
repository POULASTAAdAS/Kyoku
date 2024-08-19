package com.poulastaa.play.presentation.view

import com.poulastaa.core.ViewData
import com.poulastaa.core.domain.ViewSong

fun ViewSong.toViewUiSong() = ViewUiSong(
    id = this.id,
    name = this.name,
    coverImage = this.coverImage,
    artist = this.artist
)

fun ViewData.toViewUiSong() = ViewUiData(
    id = this.id,
    name = this.name,
    listOfSong = this.listOfSong.map { it.toViewUiSong() }
)

fun List<ViewSong>.toOtherData() = ViewUiData(
    listOfSong = this.map { it.toViewUiSong() }
)
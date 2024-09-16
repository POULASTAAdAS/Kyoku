package com.poulastaa.play.presentation.view_edit

import com.poulastaa.core.domain.model.PlaylistSong
import com.poulastaa.core.domain.model.ViewEditType
import com.poulastaa.play.presentation.view.components.ViewDataType

fun ViewDataType.toViewEditType() = when (this) {
    ViewDataType.PLAYLIST -> ViewEditType.PLAYLIST
    ViewDataType.FEV -> ViewEditType.FEV
    else -> throw Exception("This should not happen")
}

fun PlaylistSong.toViewEditUiSong() = ViewEditUiSong(
    id = this.id,
    title = this.title,
    coverImage = this.coverImage,
    artist = this.artist,
    isVisible = true
)
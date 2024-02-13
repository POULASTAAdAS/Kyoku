package com.poulastaa.kyoku.data.model.ui

import com.poulastaa.kyoku.data.model.database.SongInfo

data class UiPlaylist(
    val name: String,
    val songs: List<SongInfo>,
    var isExpanded: Boolean = false
)

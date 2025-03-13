package com.poulastaa.view.presentation.model

import com.poulastaa.core.domain.model.SongId

data class UiViewPrevSong(
    val id: SongId = -1,
    val title: String = "",
    val poster: String = "",
    val artists: String? = null,
    val isSelected: Boolean = false,
)

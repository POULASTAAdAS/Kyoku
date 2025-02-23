package com.poulastaa.core.presentation.designsystem.model

import com.poulastaa.core.domain.model.SongId

data class UiPreSong(
    val id: SongId = -1,
    val title: String = "",
    val poster: String? = null,
)

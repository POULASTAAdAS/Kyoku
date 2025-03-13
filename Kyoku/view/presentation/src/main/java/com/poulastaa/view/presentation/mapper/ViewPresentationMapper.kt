package com.poulastaa.view.presentation.mapper

import com.poulastaa.core.domain.model.DtoDetailedPrevSong
import com.poulastaa.view.presentation.model.UiViewPrevSong

internal fun DtoDetailedPrevSong.toUiViewPrevSong() = UiViewPrevSong(
    id = this.id,
    title = this.title,
    artists = this.artists,
    poster = this.poster ?: "",
)
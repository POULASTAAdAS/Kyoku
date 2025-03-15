package com.poulastaa.view.presentation.others.mapper

import com.poulastaa.core.domain.model.ViewType
import com.poulastaa.core.domain.model.DtoHeading
import com.poulastaa.view.presentation.model.UiViewType
import com.poulastaa.view.presentation.others.UiRoot

internal fun ViewType.toUiViewType() = when (this) {
    ViewType.PLAYLIST -> UiViewType.PLAYLIST
    ViewType.ALBUM -> UiViewType.ALBUM
    ViewType.FAVOURITE -> UiViewType.FAVOURITE
    ViewType.POPULAR_SONG_MIX -> UiViewType.POPULAR_SONG_MIX
    ViewType.POPULAR_YEAR_MIX -> UiViewType.POPULAR_YEAR_MIX
    ViewType.SAVED_ARTIST_SONG_MIX -> UiViewType.SAVED_ARTIST_SONG_MIX
    ViewType.DAY_TYPE_MIX -> UiViewType.DAY_TYPE_MIX
    else -> throw IllegalArgumentException("Invalid UiViewType")
}

internal fun DtoHeading.toUiRoot() = UiRoot(
    id = this.id,
    title = this.name,
    poster = this.poster
)
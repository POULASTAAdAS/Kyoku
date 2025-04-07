package com.poulastaa.view.presentation.saved

import com.poulastaa.view.domain.model.DtoViewSavedItem
import com.poulastaa.view.domain.model.DtoViewSavedItemType

internal fun ViewSavedUiItemType.toDtoViewSavedItemType() = when (this) {
    ViewSavedUiItemType.ARTIST -> DtoViewSavedItemType.ARTIST
    ViewSavedUiItemType.PLAYLIST -> DtoViewSavedItemType.PLAYLIST
    ViewSavedUiItemType.ALBUM -> DtoViewSavedItemType.ALBUM
    ViewSavedUiItemType.NONE -> throw IllegalArgumentException("Invalid DtoViewSavedItemType Type")
}

internal fun DtoViewSavedItem.toViewSavedUiItem() = ViewSavedUiItem(
    id = this.id,
    title = this.title,
    poster = this.poster,
    artist = this.artist,
    releaseYear = this.releaseYear,
    numbers = this.numbers
)
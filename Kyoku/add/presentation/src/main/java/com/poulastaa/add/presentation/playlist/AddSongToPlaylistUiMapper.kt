package com.poulastaa.add.presentation.playlist

import com.poulastaa.add.domain.model.DtoAddSongToPlaylistItem
import com.poulastaa.add.domain.model.DtoAddSongToPlaylistPageItem
import com.poulastaa.add.domain.model.DtoAddSongToPlaylistPageType
import com.poulastaa.add.domain.model.DtoAddSongToPlaylistSearchFilterType
import com.poulastaa.add.domain.model.DtoAddToPlaylistItemType

internal fun DtoAddSongToPlaylistPageItem.toAddSongToPlaylistPageUiItem() =
    AddSongToPlaylistPageUiItem(
        type = when (this.type) {
            DtoAddSongToPlaylistPageType.YOUR_FAVOURITES -> AddSongToPlaylistPageUiType.YOUR_FAVOURITES
            DtoAddSongToPlaylistPageType.SUGGESTED_FOR_YOU -> AddSongToPlaylistPageUiType.SUGGESTED_FOR_YOU
            DtoAddSongToPlaylistPageType.YOU_MAY_ALSO_LIKE -> AddSongToPlaylistPageUiType.YOU_MAY_ALSO_LIKE
        },
        data = this.data.map { it.toAddSongToPlaylistPageUiItem() }
    )

private fun DtoAddSongToPlaylistItem.toAddSongToPlaylistPageUiItem() = AddSongToPlaylistUiItem(
    id = this.id,
    title = this.title,
    poster = this.poster,
    artist = this.artist,
    numbers = this.numbers,
    type = AddToPlaylistItemUiType.SONG
)

internal fun AddSongToPlaylistSearchUiFilterType.toDtoDtoAddSongToPlaylistSearchFilterType() =
    when (this) {
        AddSongToPlaylistSearchUiFilterType.ALL -> DtoAddSongToPlaylistSearchFilterType.ALL
        AddSongToPlaylistSearchUiFilterType.ALBUM -> DtoAddSongToPlaylistSearchFilterType.ALBUM
        AddSongToPlaylistSearchUiFilterType.SONG -> DtoAddSongToPlaylistSearchFilterType.SONG
        AddSongToPlaylistSearchUiFilterType.ARTIST -> DtoAddSongToPlaylistSearchFilterType.ARTIST
        AddSongToPlaylistSearchUiFilterType.PLAYLIST -> DtoAddSongToPlaylistSearchFilterType.PLAYLIST
    }

private fun DtoAddToPlaylistItemType.toAddToPlaylistItemUiType() = when (this) {
    DtoAddToPlaylistItemType.PLAYLIST -> AddToPlaylistItemUiType.PLAYLIST
    DtoAddToPlaylistItemType.ALBUM -> AddToPlaylistItemUiType.ALBUM
    DtoAddToPlaylistItemType.ARTIST -> AddToPlaylistItemUiType.ARTIST
    DtoAddToPlaylistItemType.SONG -> AddToPlaylistItemUiType.SONG
}

internal fun DtoAddSongToPlaylistItem.toAddSongToPlaylistUiItem() = AddSongToPlaylistUiItem(
    id = this.id,
    title = this.title,
    poster = this.poster,
    artist = this.artist,
    numbers = this.numbers,
    type = this.type.toAddToPlaylistItemUiType()
)

internal fun AddSongToPlaylistPageUiType.toPageType() = when (this) {
    AddSongToPlaylistPageUiType.YOUR_FAVOURITES -> AddSongToPlaylistUiAction.PageType.YOUR_FAVOURITE
    AddSongToPlaylistPageUiType.SUGGESTED_FOR_YOU -> AddSongToPlaylistUiAction.PageType.SUGGESTED_FOR_YOU
    AddSongToPlaylistPageUiType.YOU_MAY_ALSO_LIKE -> AddSongToPlaylistUiAction.PageType.YOU_MAY_ALSO_LIKE
}

internal fun AddSongToPlaylistUiAction.PageType.toAddSongToPlaylistPageUiType() = when (this) {
    AddSongToPlaylistUiAction.PageType.YOUR_FAVOURITE -> AddSongToPlaylistPageUiType.YOUR_FAVOURITES
    AddSongToPlaylistUiAction.PageType.SUGGESTED_FOR_YOU -> AddSongToPlaylistPageUiType.SUGGESTED_FOR_YOU
    AddSongToPlaylistUiAction.PageType.YOU_MAY_ALSO_LIKE -> AddSongToPlaylistPageUiType.YOU_MAY_ALSO_LIKE
    AddSongToPlaylistUiAction.PageType.SEARCH -> throw IllegalArgumentException("Cannot convert to page type")
}
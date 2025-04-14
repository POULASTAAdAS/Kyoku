package com.poulastaa.add.presentation.playlist

import com.poulastaa.add.domain.model.DtoAddSongToPlaylistPageItem
import com.poulastaa.add.domain.model.DtoAddSongToPlaylistPageType
import com.poulastaa.add.domain.model.DtoAddSongToPlaylistItem

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
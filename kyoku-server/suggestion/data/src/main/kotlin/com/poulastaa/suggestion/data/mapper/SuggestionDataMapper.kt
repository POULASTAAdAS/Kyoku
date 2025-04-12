package com.poulastaa.suggestion.data.mapper

import com.poulastaa.core.domain.model.DtoDetailedPrevSong
import com.poulastaa.suggestion.domain.model.DtoAddSongToPlaylistItem
import com.poulastaa.suggestion.domain.model.DtoAddSongToPlaylistPageItem
import com.poulastaa.suggestion.domain.model.DtoAddSongToPlaylistPageType
import com.poulastaa.suggestion.domain.model.DtoAddToPlaylistItemType

internal fun List<DtoDetailedPrevSong>.toDtoAddSongToPlaylistPageItem(type: DtoAddSongToPlaylistPageType) =
    DtoAddSongToPlaylistPageItem(
        type = type,
        data = this.map {
            DtoAddSongToPlaylistItem(
                id = it.id,
                title = it.title,
                poster = listOf(it.poster ?: ""),
                artist = it.artists,
                type = DtoAddToPlaylistItemType.SONG
            )
        }
    )
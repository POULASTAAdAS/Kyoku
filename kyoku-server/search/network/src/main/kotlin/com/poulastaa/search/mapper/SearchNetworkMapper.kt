package com.poulastaa.search.mapper

import com.poulastaa.core.domain.model.DtoSearchItem
import com.poulastaa.search.model.*

internal fun DtoSearchItem.toResponseExploreItem() = ResponseExploreItem(
    id = this.id,
    title = this.title,
    poster = this.poster,
    releaseYear = this.releaseYear,
    artist = this.artist
)

internal fun AddSongToPlaylistSearchFilterTypeRequest.toDtoAddSongToPlaylistSearchFilterType() = when (this) {
    AddSongToPlaylistSearchFilterTypeRequest.ALL -> DtoAddSongToPlaylistSearchFilterType.ALL
    AddSongToPlaylistSearchFilterTypeRequest.ALBUM -> DtoAddSongToPlaylistSearchFilterType.ALBUM
    AddSongToPlaylistSearchFilterTypeRequest.SONG -> DtoAddSongToPlaylistSearchFilterType.SONG
    AddSongToPlaylistSearchFilterTypeRequest.ARTIST -> DtoAddSongToPlaylistSearchFilterType.ARTIST
    AddSongToPlaylistSearchFilterTypeRequest.PLAYLIST -> DtoAddSongToPlaylistSearchFilterType.PLAYLIST
}

private fun DtoAddSongToPlaylistItemType.toAddSongToPlaylistItemTypeResponse() = when (this) {
    DtoAddSongToPlaylistItemType.PLAYLIST -> AddSongToPlaylistItemTypeResponse.PLAYLIST
    DtoAddSongToPlaylistItemType.ALBUM -> AddSongToPlaylistItemTypeResponse.ALBUM
    DtoAddSongToPlaylistItemType.ARTIST -> AddSongToPlaylistItemTypeResponse.ARTIST
    DtoAddSongToPlaylistItemType.SONG -> AddSongToPlaylistItemTypeResponse.SONG
}

internal fun DtoAddSongToPlaylistItem.toAddSongToPlaylistItem() = AddSongToPlaylistItemResponse(
    id = this.id,
    title = this.title,
    poster = this.poster,
    artist = this.artist,
    numbers = this.numbers,
    type = this.type.toAddSongToPlaylistItemTypeResponse()
)
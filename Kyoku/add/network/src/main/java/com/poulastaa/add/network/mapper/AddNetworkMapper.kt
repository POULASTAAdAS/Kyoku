package com.poulastaa.add.network.mapper

import com.poulastaa.add.domain.model.DtoAddSongToPlaylistItem
import com.poulastaa.add.domain.model.DtoAddSongToPlaylistPageItem
import com.poulastaa.add.domain.model.DtoAddSongToPlaylistPageType
import com.poulastaa.add.domain.model.DtoAddToPlaylistItemType
import com.poulastaa.add.network.model.AddSongToPlaylistItemResponse
import com.poulastaa.add.network.model.AddSongToPlaylistItemTypeResponse
import com.poulastaa.add.network.model.AddSongToPlaylistPageItemResponse
import com.poulastaa.add.network.model.AddSongToPlaylistPageTypeResponse
import kotlinx.serialization.InternalSerializationApi

@OptIn(InternalSerializationApi::class)
internal fun AddSongToPlaylistPageItemResponse.toDtoAddSongToPlaylistPageItem() =
    DtoAddSongToPlaylistPageItem(
        type = when (this.type) {
            AddSongToPlaylistPageTypeResponse.YOUR_FAVOURITES -> DtoAddSongToPlaylistPageType.YOUR_FAVOURITES
            AddSongToPlaylistPageTypeResponse.SUGGESTED_FOR_YOU -> DtoAddSongToPlaylistPageType.SUGGESTED_FOR_YOU
            AddSongToPlaylistPageTypeResponse.YOU_MAY_ALSO_LIKE -> DtoAddSongToPlaylistPageType.YOU_MAY_ALSO_LIKE
        },
        data = this.data.map { it.toDtoAddSongToPlaylistItem() }
    )

@OptIn(InternalSerializationApi::class)
internal fun AddSongToPlaylistItemResponse.toDtoAddSongToPlaylistItem(): DtoAddSongToPlaylistItem =
    DtoAddSongToPlaylistItem(
        id = this.id,
        title = this.title,
        poster = this.poster,
        artist = this.artist,
        numbers = this.numbers,
        type = when (this.type) {
            AddSongToPlaylistItemTypeResponse.PLAYLIST -> DtoAddToPlaylistItemType.PLAYLIST
            AddSongToPlaylistItemTypeResponse.ALBUM -> DtoAddToPlaylistItemType.ALBUM
            AddSongToPlaylistItemTypeResponse.ARTIST -> DtoAddToPlaylistItemType.ARTIST
            AddSongToPlaylistItemTypeResponse.SONG -> DtoAddToPlaylistItemType.SONG
        }
    )
package com.poulastaa.add.presentation.playlist.artist

import com.poulastaa.add.domain.model.DtoAddSongToPlaylistArtistSearchFilterType

internal fun AddSongToPlaylistArtistSearchFilterType.toDtoAddSongToPlaylistArtistSearchFilterType() =
    when (this) {
        AddSongToPlaylistArtistSearchFilterType.ALL -> DtoAddSongToPlaylistArtistSearchFilterType.ALL
        AddSongToPlaylistArtistSearchFilterType.ALBUM -> DtoAddSongToPlaylistArtistSearchFilterType.ALBUM
        AddSongToPlaylistArtistSearchFilterType.SONG -> DtoAddSongToPlaylistArtistSearchFilterType.SONG
    }
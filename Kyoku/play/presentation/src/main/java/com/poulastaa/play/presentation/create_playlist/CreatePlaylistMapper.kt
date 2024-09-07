package com.poulastaa.play.presentation.create_playlist

import com.poulastaa.core.domain.model.CreatePlaylistPagingData
import com.poulastaa.core.domain.model.CreatePlaylistType
import com.poulastaa.core.domain.model.Song
import com.poulastaa.core.presentation.ui.model.UiSong

fun Song.toUiSong() = UiSong(
    id = this.id,
    artist = this.artistName,
    title = this.title,
    coverImage = this.coverImage
)

fun Pair<CreatePlaylistType, List<Song>>.toCreatePlaylistData() = CreatePlaylistData(
    type = this.first,
    list = this.second.map { it.toUiSong() }
)

fun CreatePlaylistPagingData.toCreatePlaylistPagingUiData() = CreatePlaylistPagingUiData(
    id = this.id,
    title = this.title,
    coverImage = this.coverImage,
    artist = this.artist,
    expandable = this.expandable,
    isArtist = this.isArtist
)

fun CreatePlaylistPagingUiData.toUiSong() = UiSong(
    id = this.id,
    artist = this.artist,
    title = this.title,
    coverImage = this.coverImage
)
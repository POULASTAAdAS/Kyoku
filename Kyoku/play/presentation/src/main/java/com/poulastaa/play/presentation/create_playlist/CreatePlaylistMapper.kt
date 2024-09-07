package com.poulastaa.play.presentation.create_playlist

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
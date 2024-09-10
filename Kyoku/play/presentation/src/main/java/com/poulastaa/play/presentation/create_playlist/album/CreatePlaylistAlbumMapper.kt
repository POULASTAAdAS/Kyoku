package com.poulastaa.play.presentation.create_playlist.album

import com.poulastaa.core.domain.model.Song
import com.poulastaa.play.presentation.create_playlist.CreatePlaylistPagingUiData

fun Song.toCreatePlaylistUiData() = CreatePlaylistPagingUiData(
    id = this.id,
    title = this.title,
    coverImage = this.coverImage,
    artist = this.artistName,
    expandable = false,
    isArtist = false
)
package com.poulastaa.board.presentation.import_playlist

import com.poulastaa.board.domain.import_playlist.model.DtoPrevPlaylist
import com.poulastaa.core.presentation.designsystem.UiPrevSong

fun DtoPrevPlaylist.toUiPrevPlaylist() = UiPreviewPlaylist(
    internalId = this.internalId,
    id = this.playlistId,
    title = this.name,
    songs = this.songs.map {
        UiPrevSong(
            internalId = it.internalId,
            id = it.songId,
            title = it.title,
            artist = it.artist,
            coverImage = it.coverImage
        )
    }
)
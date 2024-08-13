package com.poulastaa.play.presentation.add_to_playlist

import com.poulastaa.core.domain.add_to_playlist.PRESENT
import com.poulastaa.core.domain.add_to_playlist.SIZE
import com.poulastaa.core.domain.model.PrevSavedPlaylist
import com.poulastaa.play.presentation.root_drawer.toUiPrevPlaylist

fun Pair<Pair<SIZE, PRESENT>, PrevSavedPlaylist>.toPlaylistData() = UiPlaylistData(
    selectStatus = UiSelectStatus(
        old = this.first.second,
        new = this.first.second
    ),
    totalSongs = this.first.first,
    playlist = this.second.toUiPrevPlaylist()
)

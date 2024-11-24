package com.poulastaa.play.presentation.create_playlist.artist

import com.poulastaa.core.presentation.ui.model.UiArtist
import com.poulastaa.play.domain.DataLoadingState

data class CreatePlaylistArtistUiState(
    val loadingState: DataLoadingState = DataLoadingState.LOADING,
    val header: String = "",
    val artist: UiArtist = UiArtist(),
)

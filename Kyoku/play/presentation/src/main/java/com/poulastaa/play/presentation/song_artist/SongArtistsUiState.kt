package com.poulastaa.play.presentation.song_artist

import com.poulastaa.core.presentation.ui.model.UiArtist
import com.poulastaa.play.domain.DataLoadingState

data class SongArtistsUiState(
    val loadingState: DataLoadingState = DataLoadingState.LOADING,
    val header: String = "",
    val artist: List<UiArtist> = emptyList(),
)

package com.poulastaa.play.presentation.view_artist

import com.poulastaa.core.presentation.ui.model.UiArtist
import com.poulastaa.core.presentation.ui.model.ViewUiSong
import com.poulastaa.play.domain.DataLoadingState

data class ViewArtistUiState(
    val loadingState: DataLoadingState = DataLoadingState.LOADING,
    val data: UiArtistData = UiArtistData()
)

data class UiArtistData(
    val artist: UiArtist = UiArtist(),
    val listOfSong: List<ViewUiSong> = emptyList()
)

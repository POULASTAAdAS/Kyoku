package com.poulastaa.play.presentation.view_artist

import com.poulastaa.core.presentation.ui.model.ArtistUiSong
import com.poulastaa.core.presentation.ui.model.UiArtist
import com.poulastaa.play.domain.DataLoadingState

data class ViewArtistUiState(
    val isInternetError: Boolean = false,
    val loadingState: DataLoadingState = DataLoadingState.LOADING,
    val header: String = "",
    val data: UiArtistData = UiArtistData()
)

data class UiArtistData(
    val isArtistFollowed: Boolean = false,
    val popularity: Long = 0,
    val artist: UiArtist = UiArtist(),
    val listOfSong: List<ArtistUiSong> = emptyList()
)

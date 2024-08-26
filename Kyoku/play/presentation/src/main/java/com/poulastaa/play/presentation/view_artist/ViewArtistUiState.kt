package com.poulastaa.play.presentation.view_artist

import com.poulastaa.core.presentation.ui.UiText
import com.poulastaa.core.presentation.ui.model.ArtistUiSong
import com.poulastaa.core.presentation.ui.model.UiArtist
import com.poulastaa.play.domain.DataLoadingState

data class ViewArtistUiState(
    val artistId: Long = -1,
    val loadingState: DataLoadingState = DataLoadingState.LOADING,
    val header: String = "",
    val data: UiArtistData = UiArtistData(),

    val toast: ViewArtistToast = ViewArtistToast(),
)

data class UiArtistData(
    val isArtistFollowed: Boolean = false,
    val popularity: Long = 0,
    val artist: UiArtist = UiArtist(),
    val listOfSong: List<ArtistUiSong> = emptyList()
)


data class ViewArtistToast(
    val isError: Boolean = false,
    val message: UiText = UiText.DynamicString("")
)
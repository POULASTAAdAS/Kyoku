package com.poulastaa.view.presentation.artist

import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.model.UiDetailedPrevSong

internal data class ViewArtistUiState(
    val loadingType: LoadingType = LoadingType.Loading,
    val artist: UiViewArtist = UiViewArtist(),
    val mostPopularSongs: List<UiDetailedPrevSong> = emptyList(),
)

internal data class UiViewArtist(
    val id: ArtistId = -1,
    val name: String = "",
    val cover: String = "",
    val isFollowing: Boolean = false,
)
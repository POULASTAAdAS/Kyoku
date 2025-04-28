package com.poulastaa.add.presentation.album.view

import com.poulastaa.add.presentation.album.UiAlbum
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.model.UiDetailedPrevSong

internal data class AddAlbumViewUiState(
    val loadingType: LoadingType = LoadingType.Loading,
    val album: UiAlbum = UiAlbum(
        name = "That Cool Album"
    ),
    val songs: List<UiDetailedPrevSong> = emptyList(),
)

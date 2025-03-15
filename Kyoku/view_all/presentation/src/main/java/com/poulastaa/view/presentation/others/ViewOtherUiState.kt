package com.poulastaa.view.presentation.others

import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.view.presentation.model.UiViewPrevSong
import com.poulastaa.view.presentation.model.UiViewType

internal data class ViewOtherUiState(
    val loadingType: LoadingType = LoadingType.Loading,
    val type: UiViewType = UiViewType.NULL,
    val root: UiRoot = UiRoot(),
    val listOfSongs: List<UiViewPrevSong> = emptyList(),
)

internal data class UiRoot(
    val id: Long = -1,
    val title: String = "",
    val poster: String = "",
)

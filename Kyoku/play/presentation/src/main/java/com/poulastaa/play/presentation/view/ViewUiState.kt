package com.poulastaa.play.presentation.view

import com.poulastaa.core.presentation.ui.model.ViewUiSong
import com.poulastaa.play.domain.DataLoadingState


data class ViewUiState(
    val loadingState: DataLoadingState = DataLoadingState.LOADING,
    val topBarTitle: String = "",

    val isMakingAPiCall: Boolean = false,
    val header: String = "",
    val data: ViewUiData = ViewUiData()
)

data class ViewUiData(
    val id: Long = -1,
    val name: String = "",
    val urls: List<String> = emptyList(),
    val listOfSong: List<ViewUiSong> = emptyList()
)



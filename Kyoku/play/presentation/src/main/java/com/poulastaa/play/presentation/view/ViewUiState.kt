package com.poulastaa.play.presentation.view

import com.poulastaa.core.presentation.ui.model.ViewUiSong
import com.poulastaa.play.domain.DataLoadingState
import com.poulastaa.play.domain.ViewSongOperation
import com.poulastaa.play.presentation.view.components.ViewDataType


data class ViewUiState(
    val loadingState: DataLoadingState = DataLoadingState.LOADING,
    val topBarTitle: String = "",

    val type: ViewDataType = ViewDataType.ALBUM,
    val isPlayingQueue: Boolean = false,

    val isMakingAPiCall: Boolean = false,
    val header: String = "",
    val isSavedData: Boolean = true,
    val threeDotOperations: List<ViewSongOperation> = emptyList(),
    val data: ViewUiData = ViewUiData()
)

data class ViewUiData(
    val id: Long = -1,
    val name: String = "",
    val urls: List<String> = emptyList(),
    val listOfSong: List<ViewUiSong> = emptyList()
)
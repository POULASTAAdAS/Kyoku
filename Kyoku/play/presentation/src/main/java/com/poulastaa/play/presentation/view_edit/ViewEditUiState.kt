package com.poulastaa.play.presentation.view_edit

import com.poulastaa.core.domain.model.ViewEditType
import com.poulastaa.core.presentation.ui.SnackBarUiState
import com.poulastaa.play.domain.DataLoadingState

data class ViewEditUiState(
    val loadingState: DataLoadingState = DataLoadingState.LOADING,
    val header: String = "",
    val isMoving: Boolean = false,
    val data: ViewEditData = ViewEditData(),
    val toast: SnackBarUiState = SnackBarUiState(),
)

data class ViewEditData(
    val info: ViewEditUiInfo = ViewEditUiInfo(),
    val songs: List<ViewEditUiSong> = emptyList(),
)

data class ViewEditUiInfo(
    val id: Long = -1,
    val name: String = "",
    val type: ViewEditType = ViewEditType.PLAYLIST,
)

data class ViewEditUiSong(
    val id: Long = -1,
    val title: String = "",
    val coverImage: String = "",
    val artist: String = "",
    val isVisible: Boolean = true,
)
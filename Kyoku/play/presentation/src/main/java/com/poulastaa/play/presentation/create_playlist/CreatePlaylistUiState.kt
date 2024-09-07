package com.poulastaa.play.presentation.create_playlist

import com.poulastaa.core.domain.model.CreatePlaylistType
import com.poulastaa.core.presentation.ui.model.UiSong
import com.poulastaa.play.domain.DataLoadingState

data class CreatePlaylistUiState(
    val playlistId: Long = -1,

    val loadingState: DataLoadingState = DataLoadingState.LOADING,
    val generatedData: List<CreatePlaylistData> = emptyList(),

    val header: String = "",

    val searchQuery: String = "",
    val isSearchEnabled: Boolean = false
)

data class CreatePlaylistData(
    val type: CreatePlaylistType,
    val list: List<UiSong>
)

package com.poulastaa.play.presentation.add_new_album

import com.poulastaa.core.presentation.ui.SnackBarUiState
import com.poulastaa.play.domain.DataLoadingState

data class AddAlbumUiState(
    val loadingState: DataLoadingState = DataLoadingState.LOADING,
    val isSearchEnabled: Boolean = false,
    val searchQuery: String = "",
    val isMakingApiCall: Boolean = false,

    val isMassSelectEnabled: Boolean = false,

    val header: String = "",
    val threeDotOperations: List<AddAlbumOperation> = emptyList(),

    val toast: SnackBarUiState = SnackBarUiState()
)


data class AddAlbumUiAlbum(
    val id: Long = -1,
    val name: String = "",
    val coverImage: String = "",
    val artist: String = "",
    val releaseYear: String = "",
    val isExtended: Boolean = false,
    val isSelected: Boolean = false
)
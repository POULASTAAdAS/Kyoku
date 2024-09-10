package com.poulastaa.play.presentation.add_to_playlist

import com.poulastaa.core.presentation.ui.UiText
import com.poulastaa.core.presentation.ui.model.UiPrevPlaylist

data class AddToPlaylistUiState(
    val songId: Long = -1,

    val isLoading: Boolean = true,
    val isMakingApiCall: Boolean = false,
    val header: String = "",
    val isSearchEnable: Boolean = false,
    val query: String = "",

    val addNewPlaylistBottomSheetState: AddNewPlaylistBottomSheetUiState = AddNewPlaylistBottomSheetUiState(),

    val oldPlaylistData: List<UiPlaylistData> = emptyList(),
    val playlistData: List<UiPlaylistData> = emptyList(),
    val favouriteData: UiFavouriteData = UiFavouriteData()
)

data class AddNewPlaylistBottomSheetUiState(
    val isOpen: Boolean = false,
    val name: String = "",
    val isMakingApiCall: Boolean = false,
    val isValidName: Boolean = true,
    val errorMessage: UiText = UiText.DynamicString("")
)


data class UiPlaylistData(
    val selectStatus: UiSelectStatus = UiSelectStatus(),
    val totalSongs: Int = 0,
    val playlist: UiPrevPlaylist = UiPrevPlaylist()
)

data class UiFavouriteData(
    val selectStatus: UiSelectStatus = UiSelectStatus(),
    val totalSongs: Int = 0
)

data class UiSelectStatus(
    val old: Boolean = false,
    val new: Boolean = false,
)
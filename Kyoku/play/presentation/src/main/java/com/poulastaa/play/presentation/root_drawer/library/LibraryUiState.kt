package com.poulastaa.play.presentation.root_drawer.library

import com.poulastaa.core.presentation.ui.UiText
import com.poulastaa.play.presentation.root_drawer.library.model.LibraryFilterType
import com.poulastaa.play.presentation.root_drawer.library.model.LibraryUiData
import com.poulastaa.play.presentation.root_drawer.library.model.LibraryViewType

data class LibraryUiState(
    val viewTypeReading: Boolean = true,
    val isDataLoading: Boolean = true,
    val header: String = "",
    val filterType: LibraryFilterType = LibraryFilterType.ALL,
    val viewType: LibraryViewType = LibraryViewType.GRID,

    val grid: Int = 3,
    val list: Int = 1,

    val data: LibraryUiData = LibraryUiData(),
    val libraryBottomSheet: LibraryBottomSheetUiState = LibraryBottomSheetUiState(),

    val toast: LibraryUiToast = LibraryUiToast()
) {
    val canShowUi: Boolean
        get() = !isDataLoading && !viewTypeReading

    val gridSize: Int
        get() = if (viewType == LibraryViewType.GRID) this.grid else this.list
}


data class LibraryBottomSheetUiState(
    val isOpen: Boolean = false,
    val isPinned: Boolean = false,
    val id: Long = -1,
    val type: LibraryBottomSheetLongClickType = LibraryBottomSheetLongClickType.LOAD,
    val title: String = "",
    val urls: List<String> = emptyList(),
)

enum class LibraryBottomSheetLongClickType {
    LOAD,
    ALBUM,
    ARTIST,
    PLAYLIST,
    FAVOURITE
}

data class LibraryUiToast(
    val isVisible: Boolean = false,
    val message: UiText = UiText.DynamicString(""),
    val type: LibraryToastType = LibraryToastType.DEFAULT
)

enum class LibraryToastType {
    ERROR,
    SUCCESS,
    DEFAULT
}


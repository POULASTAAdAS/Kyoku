package com.poulastaa.play.presentation.root_drawer.library

import com.poulastaa.play.presentation.root_drawer.library.model.LibraryFilterType
import com.poulastaa.play.presentation.root_drawer.library.model.LibraryUiData
import com.poulastaa.play.presentation.root_drawer.library.model.LibraryViewType

data class LibraryUiState(
    val viewTypeReading: Boolean = true,
    val isDataLoading: Boolean = true,
    val header: String = "",
    val filterType: LibraryFilterType = LibraryFilterType.ALL,
    val viewType: LibraryViewType = LibraryViewType.GRID,

    val data: LibraryUiData = LibraryUiData(),
) {
    val canShowUi: Boolean
        get() = !isDataLoading && !viewTypeReading

    val gridSize: Int
        get() = if (viewType == LibraryViewType.GRID) 3 else 1
}



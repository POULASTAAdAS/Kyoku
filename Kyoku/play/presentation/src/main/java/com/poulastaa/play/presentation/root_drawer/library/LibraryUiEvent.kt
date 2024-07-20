package com.poulastaa.play.presentation.root_drawer.library

import com.poulastaa.play.presentation.root_drawer.library.model.LibraryFilterType

sealed interface LibraryUiEvent {
    data object OnSearchClick : LibraryUiEvent

    data class ToggleFilterType(val type: LibraryFilterType) : LibraryUiEvent
    data object ToggleView : LibraryUiEvent
}
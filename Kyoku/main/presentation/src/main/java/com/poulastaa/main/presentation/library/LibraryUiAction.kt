package com.poulastaa.main.presentation.library

internal sealed interface LibraryUiAction {
    data class OnFilterTypeClick(val type: FilterType) : LibraryUiAction
}
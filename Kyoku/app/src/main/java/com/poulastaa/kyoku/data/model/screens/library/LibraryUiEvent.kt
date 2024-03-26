package com.poulastaa.kyoku.data.model.screens.library

sealed class LibraryUiEvent {
    data class EmitToast(val message: String) : LibraryUiEvent()
    data object SortTypeClick : LibraryUiEvent()
    data object SomethingWentWrong : LibraryUiEvent()
}
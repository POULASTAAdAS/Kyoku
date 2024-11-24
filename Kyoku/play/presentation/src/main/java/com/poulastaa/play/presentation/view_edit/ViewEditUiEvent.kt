package com.poulastaa.play.presentation.view_edit

sealed interface ViewEditUiEvent {
    data object OnExploreClick : ViewEditUiEvent
    data class OnDeleteClick(val songId: Long) : ViewEditUiEvent
}
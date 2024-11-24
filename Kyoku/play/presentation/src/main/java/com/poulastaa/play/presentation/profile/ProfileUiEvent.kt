package com.poulastaa.play.presentation.profile

sealed interface ProfileUiEvent {
    data object EditClick: ProfileUiEvent
    data class OnItemClick(val type: ProfileItemType): ProfileUiEvent
}
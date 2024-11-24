package com.poulastaa.play.presentation.profile

sealed interface ProfileUiAction {
    data object NavigateToLibrary: ProfileUiAction
}
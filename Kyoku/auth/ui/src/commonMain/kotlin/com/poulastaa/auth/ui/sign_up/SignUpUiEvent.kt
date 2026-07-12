package com.poulastaa.auth.ui.sign_up

sealed interface SignUpUiEvent {
    data object NavigateToLogIn : SignUpUiEvent
    data object NavigateToImportPlaylist : SignUpUiEvent
    data object NavigateToHome : SignUpUiEvent
}

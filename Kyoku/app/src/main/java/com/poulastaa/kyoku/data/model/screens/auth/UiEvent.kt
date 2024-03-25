package com.poulastaa.kyoku.data.model.screens.auth

sealed class UiEvent {
    data class Navigate(val route: String) : UiEvent()
    // todo data class NavigateWithData(val route: String, val data: String) : UiEvent()
    data class ShowToast(val message: String) : UiEvent()
}

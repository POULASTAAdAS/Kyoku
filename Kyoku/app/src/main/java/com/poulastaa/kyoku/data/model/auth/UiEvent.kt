package com.poulastaa.kyoku.data.model.auth

sealed class UiEvent {
    data class Navigate(val route: String) : UiEvent()
    data class ShowToast(val message: String) : UiEvent()
}

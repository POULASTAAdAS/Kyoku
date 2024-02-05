package com.poulastaa.kyoku.data.model.auth

sealed class AuthUiEvent {
    data class Navigate(val route: String) : AuthUiEvent()
    data class ShowToast(val message: String) : AuthUiEvent()
}

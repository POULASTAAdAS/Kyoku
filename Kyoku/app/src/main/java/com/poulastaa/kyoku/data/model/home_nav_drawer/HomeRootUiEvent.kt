package com.poulastaa.kyoku.data.model.home_nav_drawer

sealed class HomeRootUiEvent {
    data class Navigate(val route: String) : HomeRootUiEvent()
    data object LogOut : HomeRootUiEvent()
}
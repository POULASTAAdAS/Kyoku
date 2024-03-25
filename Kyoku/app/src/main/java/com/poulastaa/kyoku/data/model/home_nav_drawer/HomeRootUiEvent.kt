package com.poulastaa.kyoku.data.model.home_nav_drawer


sealed class HomeRootUiEvent {
    data class Navigate(val route: String) : HomeRootUiEvent()
    data class BottomNavClick(val bottomNav: HomeScreenBottomNavigation) : HomeRootUiEvent()
    data class SearchClick(val type: SearchType) : HomeRootUiEvent()
    data object CreatePlaylistClick : HomeRootUiEvent()
    data object LogOut : HomeRootUiEvent()
}

enum class HomeScreenBottomNavigation {
    HOME_SCREEN,
    LIBRARY_SCREEN
}

enum class SearchType {
    ALL_SEARCH,
    LIBRARY_SEARCH
}
package com.poulastaa.kyoku.data.model.home_nav_drawer

import com.poulastaa.kyoku.data.model.screens.common.ItemsType
import com.poulastaa.kyoku.navigation.Screens


sealed class HomeRootUiEvent {
    data class Navigate(val route: String) : HomeRootUiEvent()

    data class NavigateWithData(
        val route: String = Screens.Home.route,
        val type: ItemsType = ItemsType.PLAYLIST,
        val id: Long = -1,
        val name: String = "name",
        val longClickType: String = "longClickType",
        val isApiCall: Boolean = false
    ) : HomeRootUiEvent()

    data class BottomNavClick(val bottomNav: HomeScreenBottomNavigation) : HomeRootUiEvent()
    data class SearchClick(val type: SearchType) : HomeRootUiEvent()
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
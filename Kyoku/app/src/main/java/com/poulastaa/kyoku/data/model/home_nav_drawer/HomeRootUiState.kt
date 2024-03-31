package com.poulastaa.kyoku.data.model.home_nav_drawer

import com.poulastaa.kyoku.navigation.Screens

data class HomeRootUiState(
    val isCookie: Boolean = false,
    val headerValue: String = "",
    val homeTopBarTitle: String = "Good Morning",
    val libraryTopBarTitle: String = "Your Library",
    val profilePicUrl: String = "",
    val userName: String = "",
    val startDestination: String = Screens.Home.route,
    val isHome: Boolean = startDestination == Screens.Home.route
)

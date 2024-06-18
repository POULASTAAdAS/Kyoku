package com.poulastaa.kyoku

data class StartRoute(
    val route: String = Screens.AUTH_ROUTE,
    val startDestination: String? = null,
)

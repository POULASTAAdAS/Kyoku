package com.poulastaa.kyoku.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.poulastaa.auth.presentation.AuthRootGraph
import com.poulastaa.core.domain.SavedScreen

@Composable
internal fun RootNavigation(
    nav: NavHostController,
    screens: Screens,
) {
    NavHost(
        navController = nav,
        startDestination = screens
    ) {
        authGraph { screen ->
            when (screen) {
                SavedScreen.INTRO -> authGraph {

                }

                SavedScreen.IMPORT_SPOTIFY_PLAYLIST,
                SavedScreen.SET_B_DATE,
                SavedScreen.PIC_GENRE,
                SavedScreen.PIC_ARTIST,
                    -> TODO("setup screens not implemented")

                SavedScreen.MAIN -> TODO("main app screens not implemented")
            }
        }
    }
}

private fun NavGraphBuilder.authGraph(
    navigate: (screen: SavedScreen) -> Unit,
) {
    composable<Screens.Auth> {
        AuthRootGraph(navigate = navigate)
    }
}
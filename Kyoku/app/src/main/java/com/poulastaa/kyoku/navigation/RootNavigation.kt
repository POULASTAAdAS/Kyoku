package com.poulastaa.kyoku.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.poulastaa.auth.presentation.AuthRootGraph
import com.poulastaa.board.presentation.import_playlist.ImportPlaylistRootScreen
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
        authGraph { authNavigableScreen ->
            nav.navigate(authNavigableScreen) {
                popUpTo(authNavigableScreen) {
                    inclusive = true
                }
            }
        }

        boardGraph(
            nac = nav,
            navigateToMain = {
                nav.navigate(Screens.Main) {
                    popUpTo(Screens.Main) {
                        inclusive = true
                    }
                }
            }
        )
    }
}

private fun NavGraphBuilder.authGraph(
    navigate: (screen: SavedScreen) -> Unit,
) {
    composable<Screens.Auth> {
        AuthRootGraph(navigate = navigate)
    }
}

private fun NavGraphBuilder.boardGraph(
    nac: NavController,
    navigateToMain: () -> Unit,
) {
    composable<Screens.SetUp.ImportSpotifyPlaylist> {
        ImportPlaylistRootScreen {
            nac.navigate(Screens.SetUp.SetBirthDate)
        }
    }

    composable<Screens.SetUp.SetBirthDate> {

    }

    composable<Screens.SetUp.PickGenre> {

    }

    composable<Screens.SetUp.PickArtist> {

    }
}
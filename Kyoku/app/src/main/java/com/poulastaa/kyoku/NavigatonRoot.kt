package com.poulastaa.kyoku

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.poulastaa.auth.presentation.intro.IntroRootScreen
import com.poulastaa.auth.presentation.intro.NavigationScreen


@Composable
fun NavigationRoot(
    navHostController: NavHostController,
    route: String,
    screen: String,
) {
    NavHost(
        navController = navHostController,
        startDestination = route
    ) {
        authGraph(navHostController, screen)
        startUpGraph(navHostController, screen)
        appGraph(navHostController, screen)
    }
}

private fun NavGraphBuilder.authGraph(
    navHostController: NavHostController,
    screen: String,
) {
    navigation(
        route = Screens.AUTH_ROUTE,
        startDestination = screen
    ) {
        composable(route = Screens.Intro.route) {
            IntroRootScreen { screen ->
                navHostController.popBackStack()
                when (screen) {
                    NavigationScreen.EMAIL_LOGIN -> {
                        navHostController.navigate(Screens.EmailLogIn.route)
                    }

                    NavigationScreen.NEW_USER -> {
                        navHostController.navigate(Screens.GetSpotifyPlaylist.route)
                    }

                    NavigationScreen.SET_B_DATE -> {
                        navHostController.navigate(Screens.SetBirthDate.route)
                    }

                    NavigationScreen.PIC_GENRE -> {
                        navHostController.navigate(Screens.PicGenre.route)
                    }

                    NavigationScreen.PIC_ARTIST -> {
                        navHostController.navigate(Screens.PicArtist.route)
                    }

                    NavigationScreen.OLD_USER -> {
                        navHostController.navigate(Screens.Home.route)
                    }
                }
            }
        }

        composable(route = Screens.EmailLogIn.route) {

        }

        composable(route = Screens.EmailSignUp.route) {

        }
    }
}

private fun NavGraphBuilder.startUpGraph(
    navHostController: NavHostController,
    screen: String,
) {
    navigation(
        route = Screens.START_UP_ROUTE,
        startDestination = screen
    ) {
        composable(route = Screens.GetSpotifyPlaylist.route) {

        }

        composable(route = Screens.SetBirthDate.route) {

        }

        composable(route = Screens.PicGenre.route) {

        }

        composable(route = Screens.PicArtist.route) {

        }
    }
}

private fun NavGraphBuilder.appGraph(
    navHostController: NavHostController,
    screen: String,
) {
    navigation(
        route = Screens.APP_ROUTE,
        startDestination = screen
    ) {

    }
}
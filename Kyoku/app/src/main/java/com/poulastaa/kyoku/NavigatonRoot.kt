package com.poulastaa.kyoku

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.poulastaa.auth.presentation.email.login.EmailLoginRootScreen
import com.poulastaa.auth.presentation.intro.IntroRootScreen
import com.poulastaa.core.domain.ScreenEnum


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
    nav: NavHostController,
    screen: String,
) {
    navigation(
        route = Screens.AUTH_ROUTE,
        startDestination = screen
    ) {
        composable(route = Screens.Intro.route) {
            IntroRootScreen { screen ->
                nav.popBackStack()

                when (screen) {
                    ScreenEnum.EMAIL_LOGIN -> {
                        nav.navigate(Screens.EmailLogIn.route)
                    }

                    ScreenEnum.GET_SPOTIFY_PLAYLIST -> {
                        nav.navigate(Screens.GetSpotifyPlaylist.route)
                    }

                    ScreenEnum.SET_B_DATE -> {
                        nav.navigate(Screens.SetBirthDate.route)
                    }

                    ScreenEnum.PIC_GENRE -> {
                        nav.navigate(Screens.PicGenre.route)
                    }

                    ScreenEnum.PIC_ARTIST -> {
                        nav.navigate(Screens.PicArtist.route)
                    }

                    ScreenEnum.HOME -> {
                        nav.navigate(Screens.Home.route)
                    }

                    else -> Unit
                }
            }
        }

        composable(route = Screens.EmailLogIn.route) {
            EmailLoginRootScreen(
                navigateToEmailSignUp = {
                    nav.popBackStack()
                    nav.navigate(Screens.EmailSignUp.route)
                },
                navigateToForgotPassword = {
                    nav.navigate(Screens.ForgotPassword.route) // todo add email if any
                },
                navigate = { screen ->
                    when (screen) {
                        ScreenEnum.EMAIL_SIGN_UP -> {
                            nav.navigate(Screens.EmailSignUp.route)
                        }

                        ScreenEnum.FORGOT_PASSWORD -> {
                            nav.navigate(Screens.ForgotPassword.route)
                        }

                        ScreenEnum.GET_SPOTIFY_PLAYLIST -> {
                            nav.navigate(Screens.GetSpotifyPlaylist.route)
                        }

                        ScreenEnum.SET_B_DATE -> {
                            nav.navigate(Screens.SetBirthDate.route)
                        }

                        ScreenEnum.PIC_GENRE -> {
                            nav.navigate(Screens.PicGenre.route)
                        }

                        ScreenEnum.PIC_ARTIST -> {
                            nav.navigate(Screens.PicArtist.route)
                        }

                        ScreenEnum.HOME -> {
                            nav.navigate(Screens.Home.route)
                        }

                        else -> Unit
                    }
                }
            )
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
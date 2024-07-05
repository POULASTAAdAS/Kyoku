package com.poulastaa.kyoku

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.poulastaa.auth.presentation.email.forgot_password.ForgotPasswordRootScreen
import com.poulastaa.auth.presentation.email.login.EmailLoginRootScreen
import com.poulastaa.auth.presentation.email.signup.EmailSignUpRootScreen
import com.poulastaa.auth.presentation.intro.IntroRootScreen
import com.poulastaa.core.domain.ScreenEnum
import com.poulastaa.setup.presentation.get_spotify_playlist.SpotifyRootScreen
import com.poulastaa.setup.presentation.set_artist.ArtistRootScreen
import com.poulastaa.setup.presentation.set_b_date.BDateRootScreen
import com.poulastaa.setup.presentation.set_genre.GenreRootScreen


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
                    nav.navigate(Screens.EmailSignUp.route) {
                        popUpTo(Screens.EmailLogIn.route) {
                            inclusive = true
                            saveState = true
                        }

                        restoreState = true
                    }
                },
                navigateToForgotPassword = {
                    nav.navigate(Screens.ForgotPassword.route) {
                        popUpTo(Screens.EmailLogIn.route) {
                            inclusive = true
                            saveState = true
                        }

                        restoreState = true
                    }
                },
                navigate = { screen ->
                    nav.popBackStack()

                    when (screen) {
                        ScreenEnum.INTRO -> nav.navigate(Screens.Intro.route)

                        ScreenEnum.GET_SPOTIFY_PLAYLIST -> nav.navigate(Screens.GetSpotifyPlaylist.route)

                        ScreenEnum.SET_B_DATE -> nav.navigate(Screens.SetBirthDate.route)

                        ScreenEnum.PIC_GENRE -> nav.navigate(Screens.PicGenre.route)

                        ScreenEnum.PIC_ARTIST -> nav.navigate(Screens.PicArtist.route)

                        ScreenEnum.HOME -> nav.navigate(Screens.Home.route)

                        else -> Unit
                    }
                }
            )
        }

        composable(route = Screens.EmailSignUp.route) {
            EmailSignUpRootScreen(
                navigateBack = {
                    nav.navigate(Screens.EmailLogIn.route) {
                        popUpTo(Screens.EmailSignUp.route) {
                            inclusive = true
                            saveState = true
                        }

                        restoreState = true
                    }
                },
                navigate = { screen ->
                    nav.popBackStack()

                    when (screen) {
                        ScreenEnum.GET_SPOTIFY_PLAYLIST -> nav.navigate(Screens.GetSpotifyPlaylist.route)

                        ScreenEnum.SET_B_DATE -> nav.navigate(Screens.SetBirthDate.route)

                        ScreenEnum.PIC_GENRE -> nav.navigate(Screens.PicGenre.route)

                        ScreenEnum.PIC_ARTIST -> nav.navigate(Screens.PicArtist.route)

                        ScreenEnum.HOME -> nav.navigate(Screens.Home.route)

                        else -> Unit
                    }
                }
            )
        }

        composable(route = Screens.ForgotPassword.route) {
            ForgotPasswordRootScreen {
                nav.popBackStack()
                nav.navigate(Screens.EmailLogIn.route)
            }
        }
    }
}

private fun NavGraphBuilder.startUpGraph(
    nav: NavHostController,
    screen: String,
) {
    navigation(
        route = Screens.START_UP_ROUTE,
        startDestination = screen
    ) {
        composable(route = Screens.GetSpotifyPlaylist.route) {
            SpotifyRootScreen {
                nav.popBackStack()
                nav.navigate(Screens.SetBirthDate.route)
            }
        }

        composable(route = Screens.SetBirthDate.route) {
            BDateRootScreen(
                navigateBack = {
                    nav.popBackStack()
                    nav.navigate(Screens.GetSpotifyPlaylist.route) {
                        popUpTo(Screens.SetBirthDate.route) {
                            inclusive = true
                            saveState = true
                        }

                        restoreState = true
                    }
                },
                navigateToPicGenre = {
                    nav.popBackStack()
                    nav.navigate(Screens.PicGenre.route)
                }
            )
        }

        composable(route = Screens.PicGenre.route) {
            GenreRootScreen {
                nav.popBackStack()
                nav.navigate(Screens.PicArtist.route)
            }
        }

        composable(route = Screens.PicArtist.route) {
            ArtistRootScreen {
                nav.popBackStack()
                nav.navigate(Screens.Home.route)
            }
        }
    }
}

private fun NavGraphBuilder.appGraph(
    nav: NavHostController,
    screen: String,
) {
    navigation(
        route = Screens.APP_ROUTE,
        startDestination = screen
    ) {
        composable(route = Screens.Home.route) {

        }

        composable(route = Screens.Library.route) {

        }
    }
}
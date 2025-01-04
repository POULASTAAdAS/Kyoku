package com.poulastaa.kyoku.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.poulastaa.auth.presentation.email.forgot_password.ForgotPasswordRootScreen
import com.poulastaa.auth.presentation.email.forgot_password.ForgotPasswordViewModel
import com.poulastaa.auth.presentation.email.login.EmailLogInRootScreen
import com.poulastaa.auth.presentation.email.signup.EmailSignUpRootScreen
import com.poulastaa.auth.presentation.intro.IntroRootScreen
import com.poulastaa.core.domain.model.SavedScreen
import com.poulastaa.settings.presentation.SettingsRootScreen
import com.poulastaa.setup.presentation.spotify_playlist.ImportPlaylistRootScreen

private const val DEFAULT_ANIMATION_TIME = 600

@Composable
fun RootNavigation(
    nav: NavHostController,
    screen: Screens,
) {
    NavHost(
        navController = nav,
        startDestination = screen
    ) {
        authGraph(nav)
        setupGraph(nav)
        coreGraph(nav)
    }
}

fun NavGraphBuilder.authGraph(nav: NavHostController) {
    composable<Screens.Auth.Intro> {
        IntroRootScreen(
            navigateToEmailLogIn = {
                nav.navigate(Screens.Auth.EmailLogIn)
            },
            navigateOnSuccess = {
                nav.popBackStack()
                when (it) {
                    SavedScreen.IMPORT_SPOTIFY_PLAYLIST -> nav.navigate(Screens.SetUp.ImportSpotifyPlaylist)
                    SavedScreen.SET_B_DATE -> nav.navigate(Screens.SetUp.SetBirthDate)
                    SavedScreen.PIC_GENRE -> nav.navigate(Screens.SetUp.PickGenre)
                    SavedScreen.PIC_ARTIST -> nav.navigate(Screens.SetUp.PickArtist)
                    SavedScreen.HOME -> nav.navigate(Screens.Core.Home)

                    else -> Unit
                }
            }
        )
    }

    composable<Screens.Auth.EmailLogIn>(
        enterTransition = {
            fadeIn(animationSpec = tween(DEFAULT_ANIMATION_TIME)) +
                    slideInHorizontally(
                        animationSpec = tween(DEFAULT_ANIMATION_TIME),
                        initialOffsetX = { it })
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(DEFAULT_ANIMATION_TIME))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(DEFAULT_ANIMATION_TIME))
        }
    ) {
        EmailLogInRootScreen(
            navigateToEmailSignUp = {
                nav.navigate(Screens.Auth.EmailSignUp)
            },
            navigateToForgotPassword = {
                nav.navigate(Screens.Auth.ForgotPassword(it))
            },
            navigateToScreen = {
                nav.popBackStack()

                when (it) {
                    SavedScreen.IMPORT_SPOTIFY_PLAYLIST -> nav.navigate(Screens.SetUp.ImportSpotifyPlaylist)
                    SavedScreen.SET_B_DATE -> nav.navigate(Screens.SetUp.SetBirthDate)
                    SavedScreen.PIC_GENRE -> nav.navigate(Screens.SetUp.PickGenre)
                    SavedScreen.PIC_ARTIST -> nav.navigate(Screens.SetUp.PickArtist)
                    SavedScreen.HOME -> nav.navigate(Screens.Core.Home)

                    else -> Unit
                }
            }
        )
    }

    composable<Screens.Auth.EmailSignUp> {
        EmailSignUpRootScreen(
            navigateToLogin = {
                nav.navigate(Screens.Auth.EmailLogIn) {
                    popUpTo(Screens.Auth.EmailLogIn) {
                        inclusive = true
                    }
                }
            },
            navigateToScreen = {
                nav.popBackStack()

                when (it) {
                    SavedScreen.IMPORT_SPOTIFY_PLAYLIST -> nav.navigate(Screens.SetUp.ImportSpotifyPlaylist)
                    SavedScreen.SET_B_DATE -> nav.navigate(Screens.SetUp.SetBirthDate)
                    SavedScreen.PIC_GENRE -> nav.navigate(Screens.SetUp.PickGenre)
                    SavedScreen.PIC_ARTIST -> nav.navigate(Screens.SetUp.PickArtist)
                    SavedScreen.HOME -> nav.navigate(Screens.Core.Home)

                    else -> Unit
                }
            }
        )
    }

    composable<Screens.Auth.ForgotPassword>(
        enterTransition = {
            fadeIn(animationSpec = tween(DEFAULT_ANIMATION_TIME)) +
                    slideInVertically(
                        animationSpec = tween(DEFAULT_ANIMATION_TIME),
                        initialOffsetY = { it })
        },
        exitTransition = {
            fadeOut(animationSpec = tween(DEFAULT_ANIMATION_TIME)) +
                    slideOutVertically(
                        animationSpec = tween(DEFAULT_ANIMATION_TIME),
                        targetOffsetY = { it }
                    )
        }
    ) { stackEntry ->
        val email = stackEntry.toRoute<Screens.Auth.ForgotPassword>().email

        val viewmodel = hiltViewModel<ForgotPasswordViewModel>()
        email?.let { viewmodel.loadEmail(it) }

        ForgotPasswordRootScreen(
            viewModel = viewmodel,
            navigateBack = {
                nav.popBackStack()
            }
        )
    }
}

fun NavGraphBuilder.setupGraph(nav: NavHostController) {
    composable<Screens.SetUp.ImportSpotifyPlaylist> {
        ImportPlaylistRootScreen {
            nav.navigate(Screens.SetUp.SetBirthDate)
        }
    }

    composable<Screens.SetUp.SetBirthDate> {
        // todo
    }

    composable<Screens.SetUp.PickGenre> {
        // todo
    }

    composable<Screens.SetUp.PickArtist> {
        // todo
    }
}

fun NavGraphBuilder.coreGraph(nav: NavHostController) {
    composable<Screens.Core.Home> {
        // todo
    }

    composable<Screens.Core.Library> {
        // todo
    }

    composable<Screens.Core.Settings> {
        SettingsRootScreen {
            nav.navigate(Screens.Auth.Intro) {
                popUpTo(Screens.Auth.Intro) {
                    inclusive = true
                }
            }
        }
    }
}
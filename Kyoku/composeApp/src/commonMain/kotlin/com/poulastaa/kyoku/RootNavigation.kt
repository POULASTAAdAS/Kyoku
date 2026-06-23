package com.poulastaa.kyoku

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.poulastaa.auth.ui.forgot_password.ForgotPasswordScreen
import com.poulastaa.auth.ui.otp.OtpScreen
import com.poulastaa.auth.ui.reset_password.ResetPasswordScreen
import com.poulastaa.auth.ui.sign_in.SignInScreen
import com.poulastaa.auth.ui.sign_up.SingUpScreen
import com.poulastaa.common.ui.LocalNavController
import com.poulastaa.common.ui.Screens
import com.poulastaa.common.ui.root.RootUiState
import com.poulastaa.setup.ui.import_spotify_playlist.ImportSpotifyPlaylist
import com.poulastaa.setup.ui.select_artist.SelectArtist
import com.poulastaa.setup.ui.select_genre.SelectGenre

private const val ANIMATION_TIME_MS = 400

@Composable
fun RootNavigation(state: RootUiState) {
    val navController = rememberNavController()

    CompositionLocalProvider(LocalNavController.provides(navController)) {
        NavHost(
            navController = navController,
            startDestination = state.startDestination
        ) {
            // auth
            navigation<Screens.AuthGraph>(
                startDestination = Screens.AuthScreens.SignIn,
            ) {
                composable<Screens.AuthScreens.SignIn> {
                    SignInScreen()
                }

                composable<Screens.AuthScreens.SignUp>(
                    enterTransition = {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(ANIMATION_TIME_MS, easing = EaseInOut)
                        )
                    },
                    popExitTransition = {
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(ANIMATION_TIME_MS, easing = EaseInOut)
                        )
                    }
                ) {
                    SingUpScreen()
                }

                composable<Screens.AuthScreens.ForgotPassword>(
                    enterTransition = {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(ANIMATION_TIME_MS, easing = EaseInOut)
                        )
                    },
                    popExitTransition = {
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Down,
                            animationSpec = tween(ANIMATION_TIME_MS, easing = EaseInOut)
                        )
                    }
                ) {
                    ForgotPasswordScreen()
                }

                composable<Screens.AuthScreens.ValidateOTP> {
                    OtpScreen()
                }

                composable<Screens.AuthScreens.ResetPassword> {
                    ResetPasswordScreen()
                }
            }

            // setup
            navigation<Screens.SetupGraph>(
                startDestination = Screens.SetupScreens.ImportPlaylist
            ) {
                composable<Screens.SetupScreens.ImportPlaylist> {
                    ImportSpotifyPlaylist()
                }

                composable<Screens.SetupScreens.SelectGenre> {
                    SelectGenre()
                }

                composable<Screens.SetupScreens.SelectArtist> {
                    SelectArtist()
                }
            }

            navigation<Screens.MainGraph>(
                startDestination = Screens.MainScreens.Home,
            ) {
                composable<Screens.MainScreens.Home> {

                }
            }
        }
    }
}
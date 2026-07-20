package com.poulastaa.kyoku

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.poulastaa.auth.ui.forgot_password.ForgotPasswordScreen
import com.poulastaa.auth.ui.otp.OtpScreen
import com.poulastaa.auth.ui.reset_password.ResetPasswordScreen
import com.poulastaa.auth.ui.sign_in.SignInScreen
import com.poulastaa.auth.ui.sign_up.SingUpScreen
import com.poulastaa.common.ui.LocalNavController
import com.poulastaa.common.ui.Screens
import com.poulastaa.common.ui.components.CommonSnackbarHost
import com.poulastaa.common.ui.root.RootUiState
import com.poulastaa.setup.ui.import_spotify_playlist.ImportSpotifyPlaylist
import com.poulastaa.setup.ui.select_artist.SelectArtist
import com.poulastaa.setup.ui.select_genre.SelectGenre

private const val ANIMATION_TIME_MS = 400

@Composable
fun RootNavigation(state: RootUiState) {
    val navController = rememberNavController()

    if (state.isLoading) {
        LoadingScreen()
        return
    }

    CompositionLocalProvider(LocalNavController.provides(navController)) {
        Box(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = state.startDestination,
                modifier = Modifier.fillMaxSize(),
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
                    ) { backStackEntry ->
                        ForgotPasswordScreen(
                            email = backStackEntry.toRoute<Screens.AuthScreens.ForgotPassword>().email,
                        )
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

            CommonSnackbarHost(modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
}

@Composable
private fun LoadingScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(40.dp),
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

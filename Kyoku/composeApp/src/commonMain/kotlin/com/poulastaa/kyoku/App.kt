package com.poulastaa.kyoku

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import com.poulastaa.auth.ui.forgot_password.ForgotPasswordScreen
import com.poulastaa.auth.ui.sign_in.SignInScreen
import com.poulastaa.auth.ui.sign_up.SingUpScreen
import com.poulastaa.common.ui.LocalNavController
import com.poulastaa.common.ui.Screens
import com.poulastaa.common.ui.root.RootUiState

private const val ANIMATION_TIME_MS = 500

@Composable
fun App(
    state: RootUiState,
) {
    val navController = rememberNavController()

    CompositionLocalProvider(LocalNavController.provides(navController)) {
        NavHost(
            navController = navController,
            startDestination = state.startDestination
        ) {
            composable<Screens.Loading> {
                Column(
                    modifier = Modifier.fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(40.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

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

            composable<Screens.AuthScreens.ForgotPassword> {
                ForgotPasswordScreen()
            }
        }
    }
}
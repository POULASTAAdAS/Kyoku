package com.poulastaa.kyoku.navigation

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.poulastaa.auth.presentation.email.login.EmailLogInRootScreen
import com.poulastaa.auth.presentation.email.signup.EmailSignUpRootScreen
import com.poulastaa.auth.presentation.intro.IntroRootScreen

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
        composable<Screens.Auth.Intro> {
            IntroRootScreen(
                navigateToEmailLogIn = {
                    nav.navigate(Screens.Auth.EmailLogIn)
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
                }
            )
        }

        composable<Screens.Auth.EmailSignUp> {
            EmailSignUpRootScreen {
                nav.popBackStack()
            }
        }

        composable<Screens.Auth.ForgotPassword> {
            val email = it.toRoute<Screens.Auth.ForgotPassword>().email

            Log.d("forgot_password", email.toString())
        }
    }
}
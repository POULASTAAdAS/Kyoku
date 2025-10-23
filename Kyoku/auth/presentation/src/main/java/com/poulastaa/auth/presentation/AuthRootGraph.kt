package com.poulastaa.auth.presentation

import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.poulastaa.auth.presentation.AuthScreens.EmailSignUp
import com.poulastaa.auth.presentation.AuthScreens.ForgotPassword
import com.poulastaa.auth.presentation.AuthScreens.Intro
import com.poulastaa.auth.presentation.AuthScreens.UpdatePassword
import com.poulastaa.auth.presentation.AuthScreens.Verify
import com.poulastaa.auth.presentation.forgot_password.ForgotPasswordRootScreen
import com.poulastaa.auth.presentation.forgot_password.model.ForgotPasswordAllowedNavigationScreens
import com.poulastaa.auth.presentation.intro.IntroRootScreen
import com.poulastaa.auth.presentation.intro.IntroViewmodel
import com.poulastaa.auth.presentation.intro.model.IntroAllowedNavigationScreens
import com.poulastaa.auth.presentation.otp.OtpRootScreen
import com.poulastaa.auth.presentation.reset_password.ResetPasswordRootScreen
import com.poulastaa.auth.presentation.singup.EmailSingUpRootScreen
import com.poulastaa.core.domain.SavedScreen
import com.poulastaa.core.presentation.designsystem.UiText
import com.poulastaa.core.presentation.ui.R

private const val DEFAULT_ANIMATION_TIME = 600

@Composable
fun AuthRootGraph(
    nav: NavHostController = rememberNavController(),
    navigate: (screen: SavedScreen) -> Unit,
) {
    NavHost(
        navController = nav,
        startDestination = Intro
    ) {
        composable<Intro> {
            val viewModel: IntroViewmodel = hiltViewModel()

            IntroRootScreen(
                viewModel = viewModel,
                navigateRoot = navigate,
                navigateLocal = { screens ->
                    when (screens) {
                        is IntroAllowedNavigationScreens.ForgotPassword -> nav.navigate(
                            ForgotPassword(
                                email = screens.email
                            )
                        )

                        is IntroAllowedNavigationScreens.SingUp -> nav.navigate(
                            EmailSignUp(
                                email = screens.email
                            )
                        )

                        is IntroAllowedNavigationScreens.App -> navigate(screens.screen.toNavigationScreen())
                    }
                }
            )
        }

        composable<EmailSignUp>(
            enterTransition = {
                fadeIn(animationSpec = tween(DEFAULT_ANIMATION_TIME)) +
                        slideInHorizontally(
                            animationSpec = tween(DEFAULT_ANIMATION_TIME),
                            initialOffsetX = { it })
            },
            exitTransition = {
                fadeOut(animationSpec = tween(DEFAULT_ANIMATION_TIME)) +
                        slideOutHorizontally(
                            animationSpec = tween(DEFAULT_ANIMATION_TIME),
                            targetOffsetX = { it }
                        )
            }
        ) {
            val email = it.toRoute<ForgotPassword>().email

            EmailSingUpRootScreen(
                email,
                navigateToSetUp = {
                    navigate(SavedScreen.IMPORT_SPOTIFY_PLAYLIST)
                }) {
                nav.popBackStack()
            }
        }

        composable<ForgotPassword>(
            enterTransition = {
                fadeIn(animationSpec = tween(DEFAULT_ANIMATION_TIME)) +
                        slideInHorizontally(
                            animationSpec = tween(DEFAULT_ANIMATION_TIME),
                            initialOffsetX = { it })
            },
            exitTransition = {
                fadeOut(animationSpec = tween(DEFAULT_ANIMATION_TIME)) +
                        slideOutHorizontally(
                            animationSpec = tween(DEFAULT_ANIMATION_TIME),
                            targetOffsetX = { it }
                        )
            }
        ) {
            val email = it.toRoute<ForgotPassword>().email
            val context = LocalContext.current

            ForgotPasswordRootScreen(
                email = email,
                navigate = { screen ->
                    when (screen) {
                        is ForgotPasswordAllowedNavigationScreens.Verify -> screen.email?.let {
                            nav.navigate(Verify(screen.email))
                        } ?: Toast.makeText(
                            context,
                            UiText.StringResource(R.string.something_went_wrong_try_again)
                                .asString(context),
                            Toast.LENGTH_LONG
                        ).show()

                        ForgotPasswordAllowedNavigationScreens.NavigateBack -> nav.popBackStack()
                    }
                },
            )
        }

        composable<Verify>(
            enterTransition = {
                fadeIn(animationSpec = tween(DEFAULT_ANIMATION_TIME)) +
                        slideInHorizontally(
                            animationSpec = tween(DEFAULT_ANIMATION_TIME),
                            initialOffsetX = { it })
            },
            exitTransition = {
                fadeOut(animationSpec = tween(DEFAULT_ANIMATION_TIME)) +
                        slideOutHorizontally(
                            animationSpec = tween(DEFAULT_ANIMATION_TIME),
                            targetOffsetX = { it }
                        )
            },
            popEnterTransition = null,
            popExitTransition = null
        ) {
            val email = it.toRoute<Verify>().email

            OtpRootScreen(
                email = email,
                navigateToUpdatePassword = { token ->
                    nav.navigate(UpdatePassword(token))
                },
                navigateBack = { nav.popBackStack() }
            )
        }

        composable<UpdatePassword>(
            enterTransition = {
                fadeIn(animationSpec = tween(DEFAULT_ANIMATION_TIME)) +
                        slideInHorizontally(
                            animationSpec = tween(DEFAULT_ANIMATION_TIME),
                            initialOffsetX = { it })
            },
            exitTransition = {
                fadeOut(animationSpec = tween(DEFAULT_ANIMATION_TIME)) +
                        slideOutHorizontally(
                            animationSpec = tween(DEFAULT_ANIMATION_TIME),
                            targetOffsetX = { it }
                        )
            }
        ) {
            val payload = it.toRoute<UpdatePassword>()

            ResetPasswordRootScreen(
                token = payload.token,
                popUpToLogIn = {
                    nav.navigate(Intro) {
                        popUpTo(Intro) {
                            inclusive = true
                        }
                    }
                },
                navigateBack = {
                    nav.popBackStack()
                }
            )
        }
    }
}
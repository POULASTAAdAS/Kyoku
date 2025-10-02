package com.poulastaa.auth.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.poulastaa.auth.presentation.AuthScreens.Verify
import com.poulastaa.auth.presentation.forgot_password.ForgotPasswordRootScreen
import com.poulastaa.auth.presentation.forgot_password.model.ForgotPasswordAllowedNavigationScreens
import com.poulastaa.auth.presentation.intro.IntroRootScreen
import com.poulastaa.auth.presentation.intro.IntroViewmodel
import com.poulastaa.auth.presentation.intro.model.IntroAllowedNavigationScreens
import com.poulastaa.auth.presentation.singup.EmailSingUpRootScreen
import com.poulastaa.core.domain.SavedScreen

@Composable
fun AuthRootGraph(
    nav: NavHostController = rememberNavController(),
    navigate: (screen: SavedScreen) -> Unit,
) {
    NavHost(
        navController = nav,
        startDestination = AuthScreens.Intro
    ) {
        composable<AuthScreens.Intro> {
            val viewModel: IntroViewmodel = hiltViewModel()

            IntroRootScreen(
                viewModel = viewModel,
                navigateRoot = navigate,
                navigateLocal = { screens ->
                    when (screens) {
                        is IntroAllowedNavigationScreens.ForgotPassword -> nav.navigate(
                            AuthScreens.ForgotPassword(
                                email = screens.email
                            )
                        )

                        is IntroAllowedNavigationScreens.SingUp -> nav.navigate(
                            AuthScreens.EmailSignUp(
                                email = screens.email
                            )
                        )
                    }
                }
            )
        }

        composable<AuthScreens.EmailSignUp> {
            val email = it.toRoute<AuthScreens.ForgotPassword>().email

            EmailSingUpRootScreen(email) {
                nav.popBackStack()
            }
        }

        composable<AuthScreens.ForgotPassword> {
            val email = it.toRoute<AuthScreens.ForgotPassword>().email

            ForgotPasswordRootScreen(
                email = email,
                navigate = { screen ->
                    when (screen) {
                        is ForgotPasswordAllowedNavigationScreens.Verify -> nav.navigate(
                            Verify(screen.email)
                        )

                        ForgotPasswordAllowedNavigationScreens.NavigateBack -> nav.popBackStack()
                    }
                },
            )
        }

        composable<AuthScreens.Verify> {
            val email = it.toRoute<AuthScreens.Verify>().email
        }
    }
}
package com.poulastaa.auth.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.poulastaa.auth.presentation.intro.IntroRootScreen
import com.poulastaa.auth.presentation.intro.IntroViewmodel
import com.poulastaa.auth.presentation.intro.model.IntroNavigationScreens
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
                        is IntroNavigationScreens.ForgotPassword -> nav.navigate(
                            AuthScreens.ForgotPassword(
                                email = screens.email
                            )
                        )

                        is IntroNavigationScreens.SingUp -> nav.navigate(
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
        }

        composable<AuthScreens.ForgotPassword> {
            val email = it.toRoute<AuthScreens.ForgotPassword>().email

        }
    }
}
package com.poulastaa.kyoku.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.poulastaa.kyoku.data.model.LocalColorScheme
import com.poulastaa.kyoku.presentation.screen.auth.email.forgot_password.ForgotPasswordScreen
import com.poulastaa.kyoku.presentation.screen.auth.email.login.EmailLoginScreen
import com.poulastaa.kyoku.presentation.screen.auth.email.signup.EmailSignUpScreen
import com.poulastaa.kyoku.presentation.screen.auth.root.RootAuthScreen
import com.poulastaa.kyoku.presentation.screen.auth.root.RootAuthViewModel

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    startDestination: String,
    changeThem: (LocalColorScheme) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screens.Auth.route) {
            RootAuthScreen(
                navigateToEmailAuth = {
                    navController.navigate(it)
                }
            )
        }

        composable(Screens.AuthEmailLogin.route) {
            EmailLoginScreen(
                navigate = {
                    navController.navigate(it)
                }
            )
        }

        composable(Screens.AuthEmailSignUp.route) {
            EmailSignUpScreen(
                navigate = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screens.ForgotPassword.route) {
            ForgotPasswordScreen(
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screens.Home.route) {

        }
    }
}
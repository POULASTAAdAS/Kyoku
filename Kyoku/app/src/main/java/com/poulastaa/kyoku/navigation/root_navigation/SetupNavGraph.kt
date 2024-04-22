package com.poulastaa.kyoku.navigation.root_navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.poulastaa.kyoku.data.model.LocalColorScheme
import com.poulastaa.kyoku.navigation.Screens
import com.poulastaa.kyoku.navigation.navigate
import com.poulastaa.kyoku.presentation.screen.auth.email.forgot_password.ForgotPasswordScreen
import com.poulastaa.kyoku.presentation.screen.auth.email.login.EmailLoginScreen
import com.poulastaa.kyoku.presentation.screen.auth.email.signup.EmailSignUpScreen
import com.poulastaa.kyoku.presentation.screen.auth.root.RootAuthScreen
import com.poulastaa.kyoku.presentation.screen.home_root.HomeRootDrawer
import com.poulastaa.kyoku.presentation.screen.setup.birth_date.SetBirthDateScreen
import com.poulastaa.kyoku.presentation.screen.setup.get_spotify_playlist.SpotifyPlaylistScreen
import com.poulastaa.kyoku.presentation.screen.setup.suggest_artist.SuggestArtistScreen
import com.poulastaa.kyoku.presentation.screen.setup.suggest_genre.SuggestGenreScreen

@OptIn(ExperimentalMaterial3Api::class)
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

        composable(Screens.GetSpotifyPlaylist.route) {
            SpotifyPlaylistScreen()
        }

        composable(Screens.SetBirthDate.route) {
            SetBirthDateScreen()
        }

        composable(Screens.SuggestGenre.route) {
            SuggestGenreScreen()
        }

        composable(Screens.SuggestArtist.route) {
            SuggestArtistScreen()
        }

        composable(Screens.HomeRoot.route) {
            HomeRootDrawer(
                changeThem = {

                }
            )
        }
    }
}
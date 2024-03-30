package com.poulastaa.kyoku.navigation.root_navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.poulastaa.kyoku.data.model.LocalColorScheme
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent
import com.poulastaa.kyoku.navigation.Screens
import com.poulastaa.kyoku.navigation.navigate
import com.poulastaa.kyoku.navigation.navigateWithData
import com.poulastaa.kyoku.presentation.screen.auth.email.forgot_password.ForgotPasswordScreen
import com.poulastaa.kyoku.presentation.screen.auth.email.login.EmailLoginScreen
import com.poulastaa.kyoku.presentation.screen.auth.email.signup.EmailSignUpScreen
import com.poulastaa.kyoku.presentation.screen.auth.root.RootAuthScreen
import com.poulastaa.kyoku.presentation.screen.home_root.HomeRootDrawer
import com.poulastaa.kyoku.presentation.screen.setup.birth_date.SetBirthDateScreen
import com.poulastaa.kyoku.presentation.screen.setup.get_spotify_playlist.SpotifyPlaylistScreen
import com.poulastaa.kyoku.presentation.screen.setup.suggest_artist.SuggestArtistScreen
import com.poulastaa.kyoku.presentation.screen.setup.suggest_genre.SuggestGenreScreen
import com.poulastaa.kyoku.presentation.screen.song_view.SongViewRootScreen
import com.poulastaa.kyoku.presentation.screen.song_view.artist.ArtistAllScreen

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
                navigate = {
                    when (it) {
                        is UiEvent.Navigate -> navController.navigate(it)
                        is UiEvent.NavigateWithData -> navController.navigateWithData(it)
                        else -> Unit
                    }
                }
            )
        }

        composable(Screens.Search.route) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Search")
            }
        }

        composable(Screens.Profile.route) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Profile")
            }
        }

        composable(Screens.Settings.route) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Settings")
            }
        }

        composable(Screens.History.route) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "History")
            }
        }

        composable(
            route = Screens.SongView.route + Screens.SongView.PARAMS,
            arguments = listOf(
                navArgument(Screens.Args.TYPE.title) {
                    type = NavType.StringType
                },
                navArgument(Screens.Args.ID.title) {
                    type = NavType.LongType
                },
                navArgument(Screens.Args.NAME.title) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val type = backStackEntry.arguments?.getString(Screens.Args.TYPE.title, "") ?: ""
            val id = backStackEntry.arguments?.getLong(Screens.Args.ID.title, -1) ?: -1
            val name = backStackEntry.arguments?.getString(Screens.Args.NAME.title, "") ?: ""

            SongViewRootScreen(
                type = type,
                id = id,
                name = name,
                navigateBack = {
                    navController.popBackStack()
                },
                navigate = {
                    when (it) {
                        is UiEvent.Navigate -> navController.navigate(it)
                        is UiEvent.NavigateWithData -> navController.navigateWithData(it)
                        else -> Unit
                    }
                }
            )
        }

        composable(
            route = Screens.AllFromArtist.route + Screens.AllFromArtist.PARAMS,
            arguments = listOf(
                navArgument(Screens.Args.NAME.title) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString(Screens.Args.NAME.title, "") ?: ""

            ArtistAllScreen(name = name)
        }


        composable(Screens.Player.route) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Player")
            }
        }

        composable(Screens.CreatePlaylist.route) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "CreatePlaylist")
            }
        }

        composable(Screens.AddArtist.route) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "AddArtist")
            }
        }

        composable(Screens.AddAlbum.route) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "AddAlbum")
            }
        }
    }
}
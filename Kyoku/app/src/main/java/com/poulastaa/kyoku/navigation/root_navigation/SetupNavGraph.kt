package com.poulastaa.kyoku.navigation.root_navigation

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.poulastaa.kyoku.presentation.screen.create_playlist.CreatePlaylistScreen
import com.poulastaa.kyoku.presentation.screen.home_root.HomeRootDrawer
import com.poulastaa.kyoku.presentation.screen.setup.birth_date.SetBirthDateScreen
import com.poulastaa.kyoku.presentation.screen.setup.get_spotify_playlist.SpotifyPlaylistScreen
import com.poulastaa.kyoku.presentation.screen.setup.suggest_artist.SuggestArtistScreen
import com.poulastaa.kyoku.presentation.screen.setup.suggest_genre.SuggestGenreScreen
import com.poulastaa.kyoku.presentation.screen.song_view.SongViewRootScreen
import com.poulastaa.kyoku.presentation.screen.song_view.artist.ArtistAllScreen
import kotlinx.coroutines.launch

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
                },
                navArgument(Screens.Args.IS_API_CALL.title) {
                    type = NavType.BoolType
                }
            )
        ) { backStackEntry ->
            val type = backStackEntry.arguments?.getString(Screens.Args.TYPE.title, "") ?: ""
            val id = backStackEntry.arguments?.getLong(Screens.Args.ID.title, -1) ?: -1
            val name = backStackEntry.arguments?.getString(Screens.Args.NAME.title, "") ?: ""
            val isApiCall = backStackEntry
                .arguments?.getBoolean(Screens.Args.IS_API_CALL.title, false) ?: false

            SongViewRootScreen(
                type = type,
                id = id,
                name = name,
                isApiCall = isApiCall,
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
                },
                navArgument(Screens.Args.IS_FOR_MORE.title) {
                    type = NavType.BoolType
                }
            )
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString(Screens.Args.NAME.title, "") ?: ""
            val isForMore = backStackEntry
                .arguments?.getBoolean(Screens.Args.IS_FOR_MORE.title, false) ?: false
            ArtistAllScreen(
                name = name,
                isFromMore = isForMore,
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


        composable(Screens.Player.route) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Player")
            }
        }

        composable(
            Screens.CreatePlaylist.route + Screens.CreatePlaylist.PARAMS,
            arguments = listOf(
                navArgument(Screens.Args.NAME.title) {
                    type = NavType.StringType
                },
                navArgument(Screens.Args.TYPE.title) {
                    type = NavType.StringType
                },
                navArgument(Screens.Args.ID.title) {
                    type = NavType.LongType
                }
            )
        ) { navBackStackEntry ->
            val name = navBackStackEntry.arguments?.getString(Screens.Args.NAME.title) ?: ""
            val type = navBackStackEntry.arguments?.getString(Screens.Args.TYPE.title) ?: ""
            val id = navBackStackEntry.arguments?.getLong(Screens.Args.ID.title, -1) ?: -1

            val show = remember {
                mutableStateOf(false)
            }

            val scope = rememberCoroutineScope()
            val context = LocalContext.current

            LaunchedEffect(key1 = name) {
                if (name.isNotEmpty()) show.value = true
                else {
                    Toast.makeText(
                        context,
                        "Opp's Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()

                    navController.popBackStack()
                }
            }



            AnimatedVisibility(
                visible = show.value,
                enter = fadeIn(
                    animationSpec = tween(durationMillis = 400)
                ) + slideInVertically(
                    animationSpec = tween(durationMillis = 400),
                    initialOffsetY = { it / 2 }
                ),
                exit = fadeOut(
                    animationSpec = tween(400)
                ) + slideOutVertically(
                    animationSpec = tween(durationMillis = 400),
                    targetOffsetY = { it / 2 }
                )
            ) {
                CreatePlaylistScreen(
                    id = id,
                    name = name,
                    type = type,
                    context = context,
                    navigateBack = {
                        scope.launch {
                            show.value = false
                            navController.popBackStack()
                        }
                    }
                )
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
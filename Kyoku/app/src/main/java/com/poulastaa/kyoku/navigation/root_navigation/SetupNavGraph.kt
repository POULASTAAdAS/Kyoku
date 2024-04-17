package com.poulastaa.kyoku.navigation.root_navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.poulastaa.kyoku.presentation.screen.edit_playlist.EditPlaylistScreen
import com.poulastaa.kyoku.presentation.screen.home_root.HomeRootDrawer
import com.poulastaa.kyoku.presentation.screen.setup.birth_date.SetBirthDateScreen
import com.poulastaa.kyoku.presentation.screen.setup.get_spotify_playlist.SpotifyPlaylistScreen
import com.poulastaa.kyoku.presentation.screen.setup.suggest_artist.SuggestArtistScreen
import com.poulastaa.kyoku.presentation.screen.setup.suggest_genre.SuggestGenreScreen
import com.poulastaa.kyoku.presentation.screen.song_view.SongViewRootScreen
import com.poulastaa.kyoku.presentation.screen.song_view.SongViewViewModel
import com.poulastaa.kyoku.presentation.screen.song_view.artist.ArtistAllScreen
import com.poulastaa.kyoku.presentation.screen.view_artist.ViewArtistScreen

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

            val viewModel: SongViewViewModel = hiltViewModel()

            SongViewRootScreen(
                viewModel = viewModel,
                type = type,
                id = id,
                name = name,
                isApiCall = isApiCall,
                navigateBack = {
                    navController.popBackStack()
                    viewModel.removeDbEntrys()
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


        composable(
            route = Screens.CreatePlaylist.route + Screens.CreatePlaylist.PARAMS,
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
            ),
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        400, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(400, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Up
                )
            },
            popEnterTransition = {
                fadeIn(
                    animationSpec = tween(
                        400, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(400, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Up
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(400)
                ) + slideOutOfContainer(
                    animationSpec = tween(400),
                    towards = AnimatedContentTransitionScope.SlideDirection.Down
                )
            },
            popExitTransition = {
                fadeOut(
                    animationSpec = tween(400)
                ) + slideOutOfContainer(
                    animationSpec = tween(400),
                    towards = AnimatedContentTransitionScope.SlideDirection.Down
                )
            }
        ) { navBackStackEntry ->
            val name = navBackStackEntry.arguments?.getString(Screens.Args.NAME.title) ?: ""
            val type = navBackStackEntry.arguments?.getString(Screens.Args.TYPE.title) ?: ""
            val id = navBackStackEntry.arguments?.getLong(Screens.Args.ID.title, -1) ?: -1

            val context = LocalContext.current

            CreatePlaylistScreen(
                id = id,
                name = name,
                type = type,
                context = context,
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }


        composable(
            route = Screens.EditPlaylist.route + Screens.EditPlaylist.PARAMS,
            arguments = listOf(
                navArgument(Screens.Args.ID.title) {
                    type = NavType.LongType
                }
            ),
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        400, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(400, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Up
                )
            },
            popEnterTransition = {
                fadeIn(
                    animationSpec = tween(
                        400, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(400, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Up
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(400)
                ) + slideOutOfContainer(
                    animationSpec = tween(400),
                    towards = AnimatedContentTransitionScope.SlideDirection.Down
                )
            },
            popExitTransition = {
                fadeOut(
                    animationSpec = tween(400)
                ) + slideOutOfContainer(
                    animationSpec = tween(400),
                    towards = AnimatedContentTransitionScope.SlideDirection.Down
                )
            }
        ) { navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getLong(Screens.Args.ID.title, -1) ?: -1

            val context = LocalContext.current

            EditPlaylistScreen(
                id = id,
                context = context,
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screens.ViewArtist.route + Screens.ViewArtist.PARAMS,
            arguments = listOf(
                navArgument(Screens.Args.ID.title) {
                    type = NavType.LongType
                }
            ),
        ) {
            val id = it.arguments?.getLong(Screens.Args.ID.title, -1) ?: -1

            ViewArtistScreen(
                id = id,
                navigate = { event ->
                    when (event) {
                        is UiEvent.Navigate -> navController.navigate(event)
                        is UiEvent.NavigateWithData -> navController.navigateWithData(event)
                        else -> Unit
                    }
                },
                navigateBack = {
                    navController.popBackStack()
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
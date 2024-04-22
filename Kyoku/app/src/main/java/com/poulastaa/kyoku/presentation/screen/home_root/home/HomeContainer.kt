package com.poulastaa.kyoku.presentation.screen.home_root.home

import android.content.Context
import android.widget.Toast
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.poulastaa.kyoku.data.model.home_nav_drawer.HomeRootUiEvent
import com.poulastaa.kyoku.data.model.home_nav_drawer.HomeRootUiState
import com.poulastaa.kyoku.data.model.home_nav_drawer.Nav
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent
import com.poulastaa.kyoku.navigation.Screens
import com.poulastaa.kyoku.navigation.navigate
import com.poulastaa.kyoku.navigation.navigateWithData
import com.poulastaa.kyoku.presentation.screen.create_playlist.CreatePlaylistScreen
import com.poulastaa.kyoku.presentation.screen.edit_playlist.EditPlaylistScreen
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.HomeTopAppBar
import com.poulastaa.kyoku.presentation.screen.home_root.library.LibraryScreen
import com.poulastaa.kyoku.presentation.screen.home_root.library.component.LibraryTopAppBar
import com.poulastaa.kyoku.presentation.screen.song_view.SongViewRootScreen
import com.poulastaa.kyoku.presentation.screen.song_view.SongViewViewModel
import com.poulastaa.kyoku.presentation.screen.song_view.artist.ArtistAllScreen
import com.poulastaa.kyoku.presentation.screen.view_artist.ViewArtistScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContainer(
    profileUrl: String,
    isCookie: Boolean,
    authHeader: String,
    navController: NavHostController,
    topAppBarScrollBehavior: TopAppBarScrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(
            rememberTopAppBarState()
        ),
    isSmallPhone: Boolean = LocalConfiguration.current.screenWidthDp <= 411,
    context: Context = LocalContext.current,
    state: HomeRootUiState,
    opnDrawer: () -> Unit,
    navigateWithUiEvent: (HomeRootUiEvent) -> Unit,
) {
    Scaffold(
        modifier = Modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
        topBar = {
            when (state.nav) {
                Nav.HOME -> HomeTopAppBar(
                    title = state.homeTopBarTitle,
                    profileUrl = profileUrl,
                    isCookie = isCookie,
                    authHeader = authHeader,
                    scrollBehavior = topAppBarScrollBehavior,
                    isSmallPhone = isSmallPhone,
                    onProfileClick = opnDrawer,
                    onSearchClick = navigateWithUiEvent
                )

                Nav.LIB -> LibraryTopAppBar(
                    title = state.libraryTopBarTitle,
                    profileUrl = profileUrl,
                    isCookie = isCookie,
                    authHeader = authHeader,
                    isSmallPhone = isSmallPhone,
                    scrollBehavior = topAppBarScrollBehavior,
                    onProfileClick = opnDrawer,
                    onSearchClick = navigateWithUiEvent
                )

                else -> Unit
            }
        }
    ) { paddingValue ->
        NavHost(
            navController = navController,
            startDestination = Screens.Home.route
        ) {
            composable(route = Screens.Home.route) {
                LaunchedEffect(key1 = Unit) {
                    navigateWithUiEvent.invoke(HomeRootUiEvent.Update(Screens.Home))
                }

                HomeScreen(
                    isCookie = isCookie,
                    authHeader = authHeader,
                    isSmallPhone = isSmallPhone,
                    context = context,
                    paddingValues = paddingValue,
                    navigate = navigateWithUiEvent
                )
            }

            composable(Screens.Library.route) {
                LaunchedEffect(key1 = Unit) {
                    navigateWithUiEvent.invoke(HomeRootUiEvent.Update(Screens.Library))
                }


                LibraryScreen(
                    isSmallPhone = isSmallPhone,
                    context = context,
                    paddingValues = paddingValue,
                    isCookie = isCookie,
                    headerValue = authHeader,
                    navigate = { event ->
                        when (event) {
                            is UiEvent.Navigate -> {
                                navigateWithUiEvent.invoke(HomeRootUiEvent.Navigate(event.route))
                            }

                            is UiEvent.NavigateWithData -> {
                                navigateWithUiEvent.invoke(
                                    HomeRootUiEvent.NavigateWithData(
                                        route = event.route,
                                        type = event.itemsType,
                                        searchType = event.searchType,
                                        id = event.id,
                                        name = event.name,
                                        longClickType = event.longClickType,
                                        isApiCall = event.isApiCall
                                    )
                                )
                            }

                            is UiEvent.ShowToast -> {
                                Toast.makeText(
                                    context,
                                    event.message,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                )
            }

            composable(Screens.Search.route) {
                LaunchedEffect(key1 = Unit) {
                    navigateWithUiEvent.invoke(HomeRootUiEvent.Update(Screens.Search))
                }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Search")
                }
            }

            composable(Screens.Profile.route) {
                LaunchedEffect(key1 = Unit) {
                    navigateWithUiEvent.invoke(HomeRootUiEvent.Update(Screens.Profile))
                }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Profile")
                }
            }


            composable(Screens.Settings.route) {
                LaunchedEffect(key1 = Unit) {
                    navigateWithUiEvent.invoke(HomeRootUiEvent.Update(Screens.Settings))
                }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Settings")
                }
            }

            composable(Screens.History.route) {
                LaunchedEffect(key1 = Unit) {
                    navigateWithUiEvent.invoke(HomeRootUiEvent.Update(Screens.History))
                }

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
                LaunchedEffect(key1 = Unit) {
                    navigateWithUiEvent.invoke(HomeRootUiEvent.Update(Screens.SongView))
                }

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
                LaunchedEffect(key1 = Unit) {
                    navigateWithUiEvent.invoke(HomeRootUiEvent.Update(Screens.AllFromArtist))
                }

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
                route = Screens.ViewArtist.route + Screens.ViewArtist.PARAMS,
                arguments = listOf(
                    navArgument(Screens.Args.ID.title) {
                        type = NavType.LongType
                    }
                ),
            ) {
                LaunchedEffect(key1 = Unit) {
                    navigateWithUiEvent.invoke(HomeRootUiEvent.Update(Screens.ViewArtist))
                }

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
                LaunchedEffect(key1 = Unit) {
                    navigateWithUiEvent.invoke(HomeRootUiEvent.Update(Screens.EditPlaylist))
                }

                val id = navBackStackEntry.arguments?.getLong(Screens.Args.ID.title, -1) ?: -1

                EditPlaylistScreen(
                    id = id,
                    context = context,
                    navigateBack = {
                        navController.popBackStack()
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
                LaunchedEffect(key1 = Unit) {
                    navigateWithUiEvent.invoke(HomeRootUiEvent.Update(Screens.CreatePlaylist))
                }

                val name = navBackStackEntry.arguments?.getString(Screens.Args.NAME.title) ?: ""
                val type = navBackStackEntry.arguments?.getString(Screens.Args.TYPE.title) ?: ""
                val id = navBackStackEntry.arguments?.getLong(Screens.Args.ID.title, -1) ?: -1

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

            composable(Screens.Player.route) {
                LaunchedEffect(key1 = Unit) {
                    navigateWithUiEvent.invoke(HomeRootUiEvent.Update(Screens.Player))
                }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Player")
                }
            }

            composable(Screens.AddArtist.route) {
                LaunchedEffect(key1 = Unit) {
                    navigateWithUiEvent.invoke(HomeRootUiEvent.Update(Screens.AddArtist))
                }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "AddArtist")
                }
            }

            composable(Screens.AddAlbum.route) {
                LaunchedEffect(key1 = Unit) {
                    navigateWithUiEvent.invoke(HomeRootUiEvent.Update(Screens.AddAlbum))
                }


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
}
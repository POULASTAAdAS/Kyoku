package com.poulastaa.kyoku.presentation.screen.home_root.home

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.poulastaa.kyoku.data.model.UiEvent
import com.poulastaa.kyoku.data.model.home_nav_drawer.HomeRootUiEvent
import com.poulastaa.kyoku.data.model.home_nav_drawer.HomeRootUiState
import com.poulastaa.kyoku.data.model.home_nav_drawer.Nav
import com.poulastaa.kyoku.navigation.Screens
import com.poulastaa.kyoku.navigation.navigate
import com.poulastaa.kyoku.navigation.navigateWithData
import com.poulastaa.kyoku.presentation.screen.create_playlist.CreatePlaylistScreen
import com.poulastaa.kyoku.presentation.screen.edit_playlist.EditPlaylistScreen
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.HomeScreenBottomBar
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.HomeTopAppBar
import com.poulastaa.kyoku.presentation.screen.home_root.library.LibraryScreen
import com.poulastaa.kyoku.presentation.screen.home_root.library.component.LibraryTopAppBar
import com.poulastaa.kyoku.presentation.screen.home_root.player.Player
import com.poulastaa.kyoku.presentation.screen.home_root.player.SmallPlayer
import com.poulastaa.kyoku.presentation.screen.song_view.SongViewRootScreen
import com.poulastaa.kyoku.presentation.screen.song_view.SongViewViewModel
import com.poulastaa.kyoku.presentation.screen.song_view.artist.ArtistAllScreen
import com.poulastaa.kyoku.presentation.screen.view_artist.ViewArtistScreen
import com.poulastaa.kyoku.ui.theme.dimens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeContainer(
    profileUrl: String,
    isCookie: Boolean,
    authHeader: String,
    playingSongId: Long,
    navController: NavHostController,
    topAppBarScrollBehavior: TopAppBarScrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(
            rememberTopAppBarState()
        ),
    isDarkThem: Boolean = isSystemInDarkTheme(),
    isSmallPhone: Boolean = LocalConfiguration.current.screenWidthDp <= 411,
    context: Context = LocalContext.current,
    state: HomeRootUiState,
    scope: CoroutineScope,
    opnDrawer: () -> Unit,
    loadAditionalInfo: () -> Unit,
    playControl: (HomeRootUiEvent) -> Unit,
    navigateWithUiEvent: (HomeRootUiEvent) -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = paddingValue.calculateBottomPadding()
                ),
            contentAlignment = Alignment.BottomCenter
        ) {
            NavHost(
                navController = navController,
                startDestination = Screens.Home.route
            ) {
                composable(route = Screens.Home.route) {
                    LaunchedEffect(key1 = Unit) {
                        navigateWithUiEvent.invoke(HomeRootUiEvent.UpdateNav(Screens.Home))
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
                        navigateWithUiEvent.invoke(HomeRootUiEvent.UpdateNav(Screens.Library))
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

                                is UiEvent.Play -> {
                                    navigateWithUiEvent.invoke(
                                        HomeRootUiEvent.Play(
                                            context = context,
                                            songId = event.songId,
                                            otherId = event.otherId,
                                            playType = event.playType
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
                        navigateWithUiEvent.invoke(HomeRootUiEvent.UpdateNav(Screens.Search))
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
                        navigateWithUiEvent.invoke(HomeRootUiEvent.UpdateNav(Screens.Profile))
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
                        navigateWithUiEvent.invoke(HomeRootUiEvent.UpdateNav(Screens.Settings))
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
                        navigateWithUiEvent.invoke(HomeRootUiEvent.UpdateNav(Screens.History))
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
                        navigateWithUiEvent.invoke(HomeRootUiEvent.UpdateNav(Screens.SongView))
                    }

                    val type =
                        backStackEntry.arguments?.getString(Screens.Args.TYPE.title, "") ?: ""
                    val id = backStackEntry.arguments?.getLong(Screens.Args.ID.title, -1) ?: -1
                    val name =
                        backStackEntry.arguments?.getString(Screens.Args.NAME.title, "") ?: ""
                    val isApiCall = backStackEntry
                        .arguments?.getBoolean(Screens.Args.IS_API_CALL.title, false) ?: false

                    val viewModel: SongViewViewModel = hiltViewModel()

                    LaunchedEffect(
                        key1 = viewModel.state.isLoading,
                        key2 = playingSongId
                    ) {
                        viewModel.savePlayingSongId(playingSongId)
                    }

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
                        navigate = { event ->
                            when (event) {
                                is UiEvent.Navigate -> navController.navigate(event)
                                is UiEvent.NavigateWithData -> navController.navigateWithData(event)
                                is UiEvent.Play -> {
                                    navigateWithUiEvent.invoke(
                                        HomeRootUiEvent.Play(
                                            context = context,
                                            songId = event.songId,
                                            songIdList = event.songIdList,
                                            otherId = event.otherId,
                                            playType = event.playType
                                        )
                                    )
                                }

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
                        navigateWithUiEvent.invoke(HomeRootUiEvent.UpdateNav(Screens.AllFromArtist))
                    }

                    val name =
                        backStackEntry.arguments?.getString(Screens.Args.NAME.title, "") ?: ""
                    val isForMore = backStackEntry
                        .arguments?.getBoolean(Screens.Args.IS_FOR_MORE.title, false) ?: false
                    ArtistAllScreen( // todo put playing albumId
                        albumId = -1,
                        songId = playingSongId,
                        name = name,
                        isFromMore = isForMore,
                        navigateBack = {
                            navController.popBackStack()
                        },
                        navigate = {
                            when (it) {
                                is UiEvent.Navigate -> navController.navigate(it)
                                is UiEvent.NavigateWithData -> navController.navigateWithData(it)
                                is UiEvent.Play -> {
                                    navigateWithUiEvent.invoke(
                                        HomeRootUiEvent.Play(
                                            context = context,
                                            songId = it.songId,
                                            songIdList = it.songIdList,
                                            otherId = it.otherId,
                                            playType = it.playType
                                        )
                                    )
                                }

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
                        navigateWithUiEvent.invoke(HomeRootUiEvent.UpdateNav(Screens.ViewArtist))
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
                        navigateWithUiEvent.invoke(HomeRootUiEvent.UpdateNav(Screens.EditPlaylist))
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
                        navigateWithUiEvent.invoke(HomeRootUiEvent.UpdateNav(Screens.CreatePlaylist))
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
                        navigateWithUiEvent.invoke(HomeRootUiEvent.UpdateNav(Screens.Player))
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
                        navigateWithUiEvent.invoke(HomeRootUiEvent.UpdateNav(Screens.AddArtist))
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
                        navigateWithUiEvent.invoke(HomeRootUiEvent.UpdateNav(Screens.AddAlbum))
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

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(color = Color.Transparent)
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                LaunchedEffect(
                    key1 = state.anchors.requireOffset().roundToInt() == 250
                ) { // why the fuck are u re-calling on them change
                    if (state.anchors.requireOffset().roundToInt() == 250) {
                        navigateWithUiEvent.invoke(
                            HomeRootUiEvent.PlayerUiEvent.CancelPlay
                        )
                    }
                }

                AnimatedVisibility(
                    modifier = Modifier
                        .padding(
                            start = MaterialTheme.dimens.medium1,
                            end = MaterialTheme.dimens.medium1
                        )
                        .offset {
                            IntOffset(
                                0,
                                state.anchors
                                    .requireOffset()
                                    .roundToInt()
                            )
                        }
                        .anchoredDraggable(state.anchors, Orientation.Vertical),
                    visible = state.player.isSmallPlayer,
                    enter = fadeIn(
                        animationSpec = tween(durationMillis = 300)
                    ) + slideInVertically(
                        animationSpec = tween(durationMillis = 300),
                        initialOffsetY = { it / 2 }
                    ),
                    exit = fadeOut(
                        animationSpec = tween(durationMillis = 300)
                    ) + slideOutVertically(
                        animationSpec = tween(durationMillis = 300),
                        targetOffsetY = { it / 2 }
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(85.dp)
                            .clip(MaterialTheme.shapes.extraSmall)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = state.player.colors
                                        .take(2)
                                        .ifEmpty {
                                            listOf(
                                                MaterialTheme.colorScheme.tertiary,
                                                MaterialTheme.colorScheme.background,
                                            )
                                        }
                                )
                            )
                            .padding(MaterialTheme.dimens.small1)
                            .navigationBarsPadding()
                            .clickable(
                                interactionSource = remember {
                                    MutableInteractionSource()
                                },
                                indication = null,
                                onClick = {
                                    scope.launch {
                                        navigateWithUiEvent.invoke(
                                            HomeRootUiEvent.PlayerUiEvent.SmallPlayerClick
                                        )
                                    }
                                }
                            ),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (state.player.isLoading) CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.background
                        )
                        else SmallPlayer(
                            brushColor = state.player.colors.take(2).ifEmpty {
                                listOf(
                                    MaterialTheme.colorScheme.tertiary,
                                    MaterialTheme.colorScheme.background,
                                )
                            },
                            isPlaying = state.player.isPlaying,
                            hasNext = state.player.hasNext,
                            hasPrev = state.player.hasPrev,
                            durationUpdate = state.player.progress,
                            playingData = state.player.playingSongData.playingSong,
                            isDarkThem = isDarkThem,
                            isCookie = state.isCookie,
                            header = state.headerValue,
                            playControl = playControl,
                            onDurationChange = {
                                navigateWithUiEvent.invoke(
                                    HomeRootUiEvent.PlayerUiEvent.SeekTo(
                                        it
                                    )
                                )
                            }
                        )
                    }
                }


                if (state.nav != Nav.NON)
                    HomeScreenBottomBar(
                        isHome = state.nav == Nav.HOME
                    ) {
                        navigateWithUiEvent.invoke(
                            HomeRootUiEvent.BottomNavClick(it)
                        )
                    }
            }

            AnimatedVisibility(
                visible = state.player.isPlayerOpen,
                enter = fadeIn(
                    animationSpec = tween(durationMillis = 400)
                ) + slideInVertically(
                    animationSpec = tween(durationMillis = 400),
                    initialOffsetY = { it / 2 }
                ),
                exit = fadeOut(
                    animationSpec = tween(durationMillis = 400)
                ) + slideOutVertically(
                    animationSpec = tween(durationMillis = 400),
                    targetOffsetY = { it / 2 }
                )
            ) {
                if (state.player.isPlayerOpen)
                    LaunchedEffect(key1 = Unit) {
                        navigateWithUiEvent.invoke(
                            HomeRootUiEvent.UpdateNav(
                                screens = Screens.Player
                            )
                        )
                    }

                Player(
                    isSmallPhone = isSmallPhone,
                    paddingValue = paddingValue,
                    player = state.player,
                    isDarkThem = isDarkThem,
                    isCookie = state.isCookie,
                    header = state.headerValue,
                    scope = scope,
                    brushColor = state.player.colors.ifEmpty {
                        listOf(
                            MaterialTheme.colorScheme.tertiary,
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.onBackground,
                        )
                    },
                    playControl = playControl,
                    navigateBack = {
                        navigateWithUiEvent.invoke(
                            HomeRootUiEvent.PlayerUiEvent.SmallPlayerClick
                        )

                        if (navController.currentDestination?.route == Screens.Home.route)
                            navigateWithUiEvent.invoke(
                                HomeRootUiEvent.UpdateNav(
                                    screens = Screens.Home
                                )
                            )
                    },
                    loadAdditionalData = loadAditionalInfo,
                    onDurationChange = {
                        navigateWithUiEvent.invoke(HomeRootUiEvent.PlayerUiEvent.SeekTo(it))
                    }
                )
            }
        }
    }
}
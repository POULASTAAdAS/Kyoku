package com.poulastaa.kyoku.presentation.screen.home_root.home

import android.content.Context
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.BottomAppBarScrollBehavior
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberBottomAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.poulastaa.kyoku.data.model.home_nav_drawer.HomeRootUiEvent
import com.poulastaa.kyoku.data.model.home_nav_drawer.HomeRootUiState
import com.poulastaa.kyoku.data.model.home_nav_drawer.SearchType
import com.poulastaa.kyoku.navigation.Screens
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.HomeScreenBottomBar
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.HomeTopAppBar
import com.poulastaa.kyoku.presentation.screen.home_root.library.LibraryScreen
import com.poulastaa.kyoku.presentation.screen.home_root.library.component.LibraryTopAppBar

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
    bottomAppBarScrollBehavior: BottomAppBarScrollBehavior =
        BottomAppBarDefaults.exitAlwaysScrollBehavior(
            rememberBottomAppBarState()
        ),
    isSmallPhone: Boolean = LocalConfiguration.current.screenWidthDp <= 411,
    context: Context = LocalContext.current,
    state: HomeRootUiState,
    opnDrawer: () -> Unit,
    navigate: (HomeRootUiEvent) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
            .nestedScroll(bottomAppBarScrollBehavior.nestedScrollConnection),
        topBar = {
            if (state.isHome)
                HomeTopAppBar(
                    title = state.homeTopBarTitle,
                    profileUrl = profileUrl,
                    isCookie = isCookie,
                    authHeader = authHeader,
                    scrollBehavior = topAppBarScrollBehavior,
                    isSmallPhone = isSmallPhone,
                    onProfileClick = opnDrawer,
                    onSearchClick = {
                        navigate.invoke(HomeRootUiEvent.SearchClick(SearchType.ALL_SEARCH))
                    }
                )
            else
                LibraryTopAppBar(
                    title = state.libraryTopBarTitle,
                    profileUrl = profileUrl,
                    isCookie = isCookie,
                    authHeader = authHeader,
                    isSmallPhone = isSmallPhone,
                    scrollBehavior = topAppBarScrollBehavior,
                    onProfileClick = opnDrawer,
                    onSearchClick = {
                        navigate.invoke(HomeRootUiEvent.SearchClick(SearchType.LIBRARY_SEARCH))
                    }
                )
        },
        bottomBar = {
            HomeScreenBottomBar(
                scrollBehavior = bottomAppBarScrollBehavior,
                isHome = state.isHome
            ) {
                navigate.invoke(HomeRootUiEvent.BottomNavClick(it))
            }
        }
    ) { paddingValue ->
        NavHost(
            navController = navController,
            startDestination = state.startDestination
        ) {
            composable(Screens.Home.route) {
                HomeScreen(
                    isCookie = isCookie,
                    authHeader = authHeader,
                    isSmallPhone = isSmallPhone,
                    context = context,
                    paddingValues = paddingValue,
                    navigate = navigate
                )
            }

            composable(Screens.Library.route) {
                LibraryScreen(
                    isSmallPhone = isSmallPhone,
                    context = context,
                    paddingValues = paddingValue,
                    isCookie = isCookie,
                    headerValue = authHeader
                ) {
                    navigate.invoke(HomeRootUiEvent.Navigate(it.route))
                }
            }
        }
    }
}
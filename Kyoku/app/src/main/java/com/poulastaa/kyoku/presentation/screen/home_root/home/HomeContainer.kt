package com.poulastaa.kyoku.presentation.screen.home_root.home

import android.content.Context
import android.widget.Toast
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.BottomAppBarScrollBehavior
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberBottomAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.poulastaa.kyoku.data.model.api.service.home.HomeType
import com.poulastaa.kyoku.data.model.home_nav_drawer.HomeRootUiEvent
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent
import com.poulastaa.kyoku.data.model.screens.home.HomeUiEvent
import com.poulastaa.kyoku.navigation.Screens
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.HomeScreenBottomBar
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.HomeTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContainer(
    title: String,
    profileUrl: String,
    isCookie: Boolean,
    authHeader: String,
    viewModel: HomeScreenViewModel,
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
    opnDrawer: () -> Unit,
    navigate: (HomeRootUiEvent) -> Unit
) {
    LaunchedEffect(key1 = viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> {
                    navigate.invoke(HomeRootUiEvent.Navigate(event.route))
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
    }

    Scaffold(
        modifier = Modifier
            .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
            .nestedScroll(bottomAppBarScrollBehavior.nestedScrollConnection),
        topBar = {
            HomeTopAppBar(
                title = title,
                profileUrl = profileUrl,
                isCookie = isCookie,
                authHeader = authHeader,
                onProfileClick = opnDrawer,
                scrollBehavior = topAppBarScrollBehavior,
                isSmallPhone = isSmallPhone,
                onSearchClick = {
                    navigate.invoke(HomeRootUiEvent.Navigate(Screens.Search.route))
                }
            )
        },
        bottomBar = {
            HomeScreenBottomBar(
                scrollBehavior = bottomAppBarScrollBehavior,
                isHome = viewModel.state.isHome
            ) {
                viewModel.onEvent(event = HomeUiEvent.BottomNavClick(it))
            }
        }
    ) {
        if (
            viewModel.state.isLoading ||
            viewModel.state.data.albumPrev.isEmpty() ||
            viewModel.state.data.fevArtistMixPrev.isEmpty() ||
            viewModel.state.data.artistPrev.isEmpty()
        ) HomeScreenContentLoading(it, isSmallPhone)
        else {
            when (viewModel.state.dataType) {
                HomeType.NEW_USER_REQ -> {
                    HomeScreenContentNewUser(
                        paddingValues = it,
                        isSmallPhone = isSmallPhone,
                        data = viewModel.state.data,

                        isInternetError = viewModel.state.isInternetError,
                        errorMessage = viewModel.state.errorMessage,
                        isCookie = isCookie,
                        headerValue = authHeader
                    )
                }

                else -> {
                    if (!viewModel.state.data.favourites &&
                        viewModel.state.data.playlist.isEmpty()
                    ) HomeScreenContentNewUser(
                        paddingValues = it,
                        isSmallPhone = isSmallPhone,
                        data = viewModel.state.data,

                        isInternetError = viewModel.state.isInternetError,
                        errorMessage = viewModel.state.errorMessage,
                        isCookie = isCookie,
                        headerValue = authHeader
                    ) else
                        HomeScreenContentOldUser(
                            paddingValues = it,
                            isSmallPhone = isSmallPhone,
                            data = viewModel.state.data,

                            isInternetError = viewModel.state.isInternetError,
                            errorMessage = viewModel.state.errorMessage,
                            isCookie = isCookie,
                            headerValue = authHeader
                        )
                }
            }
        }
    }
}


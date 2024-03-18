package com.poulastaa.kyoku.presentation.screen.home_root.home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import com.poulastaa.kyoku.data.model.home_nav_drawer.HomeRootUiEvent
import com.poulastaa.kyoku.navigation.Screens
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.HomeTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    title: String,
    profileUrl: String,
    isCookie: Boolean,
    authHeader: String,
    viewModel: HomeScreenViewModel,
    opnDrawer: () -> Unit,
    navigate: (HomeRootUiEvent) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val isSmallPhone = LocalConfiguration.current.screenWidthDp <= 411

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            HomeTopAppBar(
                title = title,
                profileUrl = profileUrl,
                isCookie = isCookie,
                authHeader = authHeader,
                onProfileClick = opnDrawer,
                scrollBehavior = scrollBehavior,
                isSmallPhone = isSmallPhone,
                onSearchClick = {
                    navigate.invoke(HomeRootUiEvent.Navigate(Screens.Search.route))
                }
            )
        },
        bottomBar = {

        }
    ) {
        if (viewModel.state.isLoading) HomeScreenContentLoading(it, isSmallPhone)
        else HomeScreenContent(it, isSmallPhone)
    }
}


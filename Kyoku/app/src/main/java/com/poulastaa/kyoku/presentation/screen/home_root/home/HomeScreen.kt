package com.poulastaa.kyoku.presentation.screen.home_root.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.poulastaa.kyoku.data.model.home_nav_drawer.HomeRootUiEvent
import com.poulastaa.kyoku.navigation.Screens
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.HomeTopAppBar
import com.poulastaa.kyoku.ui.theme.dimens

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
    Scaffold(
        topBar = {
            HomeTopAppBar(
                title = title,
                profileUrl = profileUrl,
                isCookie = isCookie,
                authHeader = authHeader,
                onProfileClick = opnDrawer,
                onSearchClick = {
                    navigate.invoke(HomeRootUiEvent.Navigate(Screens.Search.route))
                }
            )
        },
        bottomBar = {

        }
    ) {
        HomeScreenContent(paddingValues = it)
    }
}

@Composable
fun HomeScreenContent(
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
                start = MaterialTheme.dimens.medium1,
                end = MaterialTheme.dimens.medium1
            )
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Home",
            fontSize = MaterialTheme.typography.headlineLarge.fontSize
        )
    }
}
package com.poulastaa.play.presentation.root_drawer.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.play.domain.HomeToDrawerEvent
import com.poulastaa.play.presentation.root_drawer.home.components.HomeAppbar

@Composable
fun HomeCompactScreen(
    profileUrl: String,
    viewModel: HomeViewModel = hiltViewModel(),
    onEvent: (HomeToDrawerEvent) -> Unit,
) {
    HomeScreen(
        profileUrl = profileUrl,
        state = viewModel.state,
        onProfileClick = {
            onEvent(HomeToDrawerEvent.PROFILE_CLICK)
        },
        onSearchClick = {
            onEvent(HomeToDrawerEvent.SEARCH_CLICK)
        },
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun HomeScreen(
    state: HomeUiState,
    profileUrl: String,
    onProfileClick: () -> Unit,
    onSearchClick: () -> Unit,
    onEvent: (HomeUiEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            HomeAppbar(
                title = state.heading,
                profileUrl = profileUrl,
                onProfileClick = onProfileClick,
                onSearchClick = onSearchClick
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surfaceContainer)
                .padding(it)
        ) {

        }
    }
}
package com.poulastaa.play.presentation.root_drawer.home

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.play.domain.HomeToDrawerEvent
import com.poulastaa.play.presentation.root_drawer.home.components.HomeScreen

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
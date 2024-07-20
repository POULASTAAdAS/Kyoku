package com.poulastaa.play.presentation.root_drawer.home

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.play.domain.TopBarToDrawerEvent
import com.poulastaa.play.presentation.root_drawer.home.components.HomeScreen

@Composable
fun HomeCompactScreen(
    profileUrl: String,
    viewModel: HomeViewModel = hiltViewModel(),
    onEvent: (TopBarToDrawerEvent) -> Unit,
) {
    HomeScreen(
        profileUrl = profileUrl,
        state = viewModel.state,
        onProfileClick = {
            onEvent(TopBarToDrawerEvent.PROFILE_CLICK)
        },
        onSearchClick = {
            onEvent(TopBarToDrawerEvent.SEARCH_CLICK)
        },
        onEvent = viewModel::onEvent
    )
}
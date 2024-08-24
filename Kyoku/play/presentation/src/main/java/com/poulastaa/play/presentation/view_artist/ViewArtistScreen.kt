package com.poulastaa.play.presentation.view_artist

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.play.domain.DataLoadingState

@Composable
fun ViewArtistRootScreen(
    modifier: Modifier = Modifier,
    artistId: Long,
    viewModel: ViewArtistViewModel = hiltViewModel(),
    navigateToArtistDetail: (artistId: Long) -> Unit,
    navigateBack: () -> Unit
) {
    ViewArtistScreen(
        state = viewModel.state,
        onEvent = viewModel::onEvent,
        navigateToArtistDetail = navigateToArtistDetail,
        navigateBack = navigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ViewArtistScreen(
    modifier: Modifier = Modifier,
    state: ViewArtistUiState,
    onEvent: (ViewArtistUiEvent) -> Unit,
    navigateToArtistDetail: (artistId: Long) -> Unit,
    navigateBack: () -> Unit
) {
    val scroll = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {

        }
    ) { innerPadding ->
        AnimatedContent(
            targetState = state.loadingState,
            label = "view artist animation transition"
        ) {
            when (it) {
                DataLoadingState.LOADING -> Column(
                    modifier = Modifier.padding(innerPadding)
                ) {

                }

                DataLoadingState.LOADED -> LazyColumn(
                    modifier = Modifier.padding(innerPadding)
                ) {

                }

                DataLoadingState.ERROR -> Column(
                    modifier = Modifier.padding(innerPadding)
                ) {

                }
            }
        }
    }
}
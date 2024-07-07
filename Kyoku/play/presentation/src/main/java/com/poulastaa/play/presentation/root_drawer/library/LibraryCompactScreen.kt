package com.poulastaa.play.presentation.root_drawer.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.core.domain.ScreenEnum
import com.poulastaa.core.presentation.ui.ObserveAsEvent
import com.poulastaa.play.presentation.root_drawer.library.components.LibraryTopAppbar

@Composable
fun LibraryCompactScreen(
    viewModel: LibraryViewModel = hiltViewModel(),
    navigate: (ScreenEnum) -> Unit,
) {
    ObserveAsEvent(flow = viewModel.uiEvent) { event ->
        when (event) {
            is LibraryUiAction.Navigate -> {
                navigate(event.screen)
            }
        }
    }

    LibraryScreen(
        state = viewModel.state,
        onSearchClick = {

        },
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LibraryScreen(
    state: LibraryUiState,
    onSearchClick: () -> Unit,
    onEvent: (LibraryUiEvent) -> Unit,
) {
    val appBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            LibraryTopAppbar(
                scrollBehavior = appBarScrollBehavior,
                onSearchClick = onSearchClick
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surfaceContainer)
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

        }
    }
}
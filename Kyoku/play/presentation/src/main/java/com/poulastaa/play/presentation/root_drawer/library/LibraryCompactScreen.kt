package com.poulastaa.play.presentation.root_drawer.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.core.domain.ScreenEnum
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.ObserveAsEvent
import com.poulastaa.play.presentation.root_drawer.library.components.LibraryFilterRow
import com.poulastaa.play.presentation.root_drawer.library.components.LibraryHeader
import com.poulastaa.play.presentation.root_drawer.library.components.LibraryTopAppbar

@Composable
fun LibraryCompactScreen(
    profileUrl: String,
    viewModel: LibraryViewModel = hiltViewModel(),
    onProfileClick: () -> Unit,
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
        profileUrl = profileUrl,
        onProfileClick = onProfileClick,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LibraryScreen(
    state: LibraryUiState,
    profileUrl: String,
    onProfileClick: () -> Unit,
    onEvent: (LibraryUiEvent) -> Unit,
) {
    val appBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            LibraryTopAppbar(
                scrollBehavior = appBarScrollBehavior,
                profileUrl = profileUrl,
                onProfileClick = onProfileClick,
                onSearchClick = {

                }
            )
        }
    ) {
        if (state.canShowUi) Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surfaceContainer)
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary.copy(.7f),
                strokeWidth = 4.dp,
                modifier = Modifier.size(56.dp),
                strokeCap = StrokeCap.Round
            )
        } else LazyVerticalGrid(
            columns = GridCells.Fixed(state.gridSize),
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surfaceContainer)
                .padding(it),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1),
            contentPadding = PaddingValues(MaterialTheme.dimens.medium1)
        ) {
            libraryItem(span = GridItemSpan(state.gridSize)) {
                LibraryFilterRow(
                    modifier = Modifier.fillMaxWidth(),
                    filterType = state.filterType,
                    viewType = state.viewType,
                    onClick = onEvent
                )
            }

            libraryItem(span = GridItemSpan(state.gridSize)) {
                LibraryHeader(header = stringResource(id = R.string.artist)) {

                }
            }
        }
    }
}

private fun LazyGridScope.libraryItem(
    span: GridItemSpan,
    content: @Composable LazyGridItemScope. () -> Unit,
) {
    item(
        span = { span },
        content = content
    )
}
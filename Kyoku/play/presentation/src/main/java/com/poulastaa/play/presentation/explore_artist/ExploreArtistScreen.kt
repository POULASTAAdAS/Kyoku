package com.poulastaa.play.presentation.explore_artist

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.core.presentation.ui.ObserveAsEvent

@Composable
fun ExploreArtistRootScreen(
    modifier: Modifier = Modifier,
    artistId: Long,
    viewModel: ExploreArtistViewModel = hiltViewModel(),
    navigate: (ExploreArtistOtherScreen) -> Unit,
    navigateBack: () -> Unit
) {
    val context = LocalContext.current

    ObserveAsEvent(flow = viewModel.uiEvent) { event ->
        when (event) {
            is ExploreArtistUiAction.EmitToast -> Toast.makeText(
                context,
                event.message.asString(context),
                Toast.LENGTH_LONG
            ).show()

            is ExploreArtistUiAction.Navigate -> navigate(event.screen)
        }
    }

    LaunchedEffect(key1 = artistId) {
        viewModel.loadData(artistId)
    }

    ExploreArtistScreen(
        modifier = modifier,
        state = viewModel.state,
        onEvent = viewModel::onEvent,
        navigateBack = navigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreArtistScreen(
    modifier: Modifier = Modifier,
    state: ExploreArtistUiState,
    onEvent: (ExploreArtistUiEvent) -> Unit,
    navigateBack: () -> Unit
) {
    val scroll = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceContainer),
        topBar = {

        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .nestedScroll(scroll.nestedScrollConnection)
        ) {

        }
    }
}
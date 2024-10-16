package com.poulastaa.play.presentation.view_artist

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.core.presentation.designsystem.components.ExpandedErrorScreen
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.ObserveAsEvent
import com.poulastaa.play.domain.DataLoadingState
import com.poulastaa.play.presentation.ArtistSongDetailsCard
import com.poulastaa.play.presentation.root_drawer.library.components.ImageGrid
import com.poulastaa.play.presentation.view_artist.components.ExploreArtistButton
import com.poulastaa.play.presentation.view_artist.components.ViewArtistExpandedLoading
import com.poulastaa.play.presentation.view_artist.components.ViewArtistNameRow
import com.poulastaa.play.presentation.view_artist.components.ViewArtistTopBar


@Composable
fun ViewArtistExpandedRootScreen(
    modifier: Modifier = Modifier,
    artistId: Long,
    viewModel: ViewArtistViewModel = hiltViewModel(),
    navigateToArtistDetail: (id: Long) -> Unit,
    navigate: (ViewArtistOtherScreen) -> Unit,
    navigateBack: () -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = artistId) {
        viewModel.loadData(artistId)
    }

    ObserveAsEvent(flow = viewModel.uiEvent) {
        when (it) {
            is ViewArtistUiAction.EmitToast -> Toast.makeText(
                context,
                it.message.asString(context),
                Toast.LENGTH_LONG
            ).show()

            is ViewArtistUiAction.Navigate -> navigate(it.screen)
        }
    }

    ViewArtistScreen(
        modifier = modifier,
        state = viewModel.state,
        onEvent = viewModel::onEvent,
        navigateToArtistDetail = {
            navigateToArtistDetail(viewModel.state.artistId)
        },
        navigateBack = navigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ViewArtistScreen(
    modifier: Modifier = Modifier,
    state: ViewArtistUiState,
    onEvent: (ViewArtistUiEvent) -> Unit,
    navigateToArtistDetail: () -> Unit,
    navigateBack: () -> Unit,
) {
    val scroll = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier,
        topBar = {
            ViewArtistTopBar(
                scrollBehavior = scroll,
                title = state.data.artist.name
            ) {
                navigateBack()
            }
        }
    ) { innerPadding ->
        AnimatedContent(
            targetState = state.loadingState,
            label = "view artist animation transition"
        ) {
            when (it) {
                DataLoadingState.LOADING -> ViewArtistExpandedLoading(
                    modifier = Modifier.padding(innerPadding)
                )

                DataLoadingState.LOADED -> Content(
                    paddingValues = innerPadding,
                    scrollBehavior = scroll,
                    state = state,
                    navigateToArtistDetail = navigateToArtistDetail,
                    onEvent = onEvent
                )

                DataLoadingState.ERROR -> ExpandedErrorScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    scrollBehavior: TopAppBarScrollBehavior,
    paddingValues: PaddingValues,
    state: ViewArtistUiState,
    navigateToArtistDetail: () -> Unit,
    onEvent: (ViewArtistUiEvent) -> Unit,
) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(.4f),
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.dimens.large2),
                shape = MaterialTheme.shapes.small,
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                )
            ) {
                ImageGrid(
                    header = state.header,
                    urls = listOf(state.data.artist.coverImageUrl)
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))

            ViewArtistNameRow(
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.medium1),
                popularity = state.data.popularity,
                name = state.data.artist.name,
                isArtistFollowed = state.data.isArtistFollowed,
                onFollowArtistToggle = {
                    onEvent(ViewArtistUiEvent.FollowArtistToggleClick)
                }
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

            ExploreArtistButton(
                modifier = Modifier.fillMaxWidth(.6f),
                name = state.data.artist.name,
                onCLick = navigateToArtistDetail
            )
        }

        LazyColumn(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            contentPadding = PaddingValues(MaterialTheme.dimens.medium1),
        ) {
            items(state.data.listOfSong) { song ->
                ArtistSongDetailsCard(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.extraSmall)
                        .clickable {
                            onEvent(ViewArtistUiEvent.OnSongClick(song.id))
                        },
                    header = state.header,
                    list = state.threeDotOperations,
                    song = song,
                    onEvent = onEvent
                )
            }
        }
    }
}
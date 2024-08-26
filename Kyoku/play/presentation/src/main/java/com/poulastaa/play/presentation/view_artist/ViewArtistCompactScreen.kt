package com.poulastaa.play.presentation.view_artist

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.core.presentation.designsystem.components.CompactErrorScreen
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.play.domain.DataLoadingState
import com.poulastaa.play.presentation.ArtistSongDetailsCard
import com.poulastaa.play.presentation.explore_artist.ExploreArtistRootScreen
import com.poulastaa.play.presentation.root_drawer.library.components.ImageGrid
import com.poulastaa.play.presentation.view_artist.components.ExploreArtistButton
import com.poulastaa.play.presentation.view_artist.components.ViewArtistCompactLoading
import com.poulastaa.play.presentation.view_artist.components.ViewArtistNameRow
import com.poulastaa.play.presentation.view_artist.components.ViewArtistTopBar

@Composable
fun ViewArtistCompactRootScreen(
    modifier: Modifier = Modifier,
    artistId: Long,
    viewModel: ViewArtistViewModel = hiltViewModel(),
    onArtistDetailScreenOpen: () -> Unit,
    navigateBack: () -> Unit
) {
    LaunchedEffect(key1 = artistId) {
        viewModel.loadData(artistId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        ViewArtistScreen(
            modifier = modifier,
            state = viewModel.state,
            onEvent = viewModel::onEvent,
            navigateBack = navigateBack
        )

        AnimatedVisibility(
            visible = viewModel.state.isExploreArtistOpen,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
        ) {
            if (viewModel.state.isExploreArtistOpen) {
                ExploreArtistRootScreen(
                    artistId = viewModel.state.artistId,
                    navigate = {

                    },
                    navigateBack = {
                        viewModel.onEvent(ViewArtistUiEvent.ExploreArtistCloseClick)
                    }
                )

                onArtistDetailScreenOpen()
            }
        }
    }

    if (viewModel.state.isExploreArtistOpen) BackHandler {
        viewModel.onEvent(ViewArtistUiEvent.ExploreArtistCloseClick)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ViewArtistScreen(
    modifier: Modifier = Modifier,
    state: ViewArtistUiState,
    onEvent: (ViewArtistUiEvent) -> Unit,
    navigateBack: () -> Unit
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
                DataLoadingState.LOADING -> ViewArtistCompactLoading(
                    modifier = Modifier.padding(innerPadding)
                )

                DataLoadingState.LOADED -> Content(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .padding(innerPadding),
                    scrollBehavior = scroll,
                    state = state,
                    onEvent = onEvent
                )

                DataLoadingState.ERROR -> CompactErrorScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    state: ViewArtistUiState,
    onEvent: (ViewArtistUiEvent) -> Unit
) {
    val config = LocalConfiguration.current

    LazyColumn(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        contentPadding = PaddingValues(MaterialTheme.dimens.medium1),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Card(
                modifier = Modifier
                    .then(
                        if (config.screenWidthDp > 680) Modifier.size(400.dp)
                        else Modifier.padding(MaterialTheme.dimens.large2)
                    ),
                shape = MaterialTheme.shapes.small,
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                )
            ) {
                ImageGrid(
                    modifier = Modifier.aspectRatio(1f),
                    header = state.header,
                    urls = listOf(state.data.artist.coverImageUrl)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))
        }

        item {
            ViewArtistNameRow(
                popularity = state.data.popularity,
                name = state.data.artist.name,
                isArtistFollowed = state.data.isArtistFollowed,
                onFollowArtistToggle = {
                    onEvent(ViewArtistUiEvent.FollowArtistToggleClick)
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
        }

        item {
            ExploreArtistButton(
                modifier = Modifier.fillMaxWidth(.5f),
                name = state.data.artist.name
            ) {
                onEvent(ViewArtistUiEvent.ExploreArtistOpenClick)
            }
        }

        item {
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
        }

        items(state.data.listOfSong) { song ->
            ArtistSongDetailsCard(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.extraSmall)
                    .clickable {
                        onEvent(ViewArtistUiEvent.OnSongClick(song.id))
                    },
                header = state.header,
                song = song,
                onThreeDotCLick = {

                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(.7f)
            ) {
                ExploreArtistButton(
                    modifier = Modifier
                        .padding(MaterialTheme.dimens.small3),
                    name = state.data.artist.name
                ) {
                    onEvent(ViewArtistUiEvent.ExploreArtistOpenClick)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
        }
    }
}
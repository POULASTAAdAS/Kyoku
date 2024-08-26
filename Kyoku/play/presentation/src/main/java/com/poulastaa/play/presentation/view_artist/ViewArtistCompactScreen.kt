package com.poulastaa.play.presentation.view_artist

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.FollowArtistIcon
import com.poulastaa.core.presentation.designsystem.UnFollowArtistIcon
import com.poulastaa.core.presentation.designsystem.components.ErrorScreen
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.model.ArtistUiSong
import com.poulastaa.core.presentation.ui.model.UiArtist
import com.poulastaa.play.domain.DataLoadingState
import com.poulastaa.play.presentation.ArtistSongDetailsCard
import com.poulastaa.play.presentation.root_drawer.library.components.ImageGrid
import com.poulastaa.play.presentation.view_artist.components.ViewArtistCompactLoading
import com.poulastaa.play.presentation.view_artist.components.ViewArtistTopBar
import kotlinx.coroutines.delay

@Composable
fun ViewArtistCompactRootScreen(
    modifier: Modifier = Modifier,
    artistId: Long,
    viewModel: ViewArtistViewModel = hiltViewModel(),
    navigateToArtistDetail: (artistId: Long) -> Unit,
    navigateBack: () -> Unit
) {
    LaunchedEffect(key1 = artistId) {
        viewModel.loadData(artistId)
    }

    ViewArtistScreen(
        modifier = modifier,
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
        modifier = modifier,
        topBar = {
            ViewArtistTopBar(scrollBehavior = scroll) {
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
                    state = state,
                    navigateToArtistDetail = navigateToArtistDetail,
                    onEvent = onEvent
                )

                DataLoadingState.ERROR -> ErrorScreen()
            }
        }
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    state: ViewArtistUiState,
    navigateToArtistDetail: (artistId: Long) -> Unit,
    onEvent: (ViewArtistUiEvent) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(MaterialTheme.dimens.medium1)
    ) {
        item {
            Card(
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(MaterialTheme.dimens.large1),
                shape = MaterialTheme.shapes.small
            ) {
                ImageGrid(
                    header = state.header,
                    urls = listOf(state.data.artist.coverImageUrl)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(.8f),
                ) {
                    Text(
                        text = state.data.artist.name,
                        fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = .8f)
                    )

                    Text(
                        text = "${state.data.popularity}  Followers",
                        color = MaterialTheme.colorScheme.onBackground.copy(.6f)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    onClick = {
                        onEvent(ViewArtistUiEvent.FollowArtistToggleClick)
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = if (state.data.isArtistFollowed)
                            Color.Transparent
                        else MaterialTheme.colorScheme.onBackground.copy(.2f),
                        contentColor = if (state.data.isArtistFollowed)
                            MaterialTheme.colorScheme.onBackground.copy(.7f)
                        else MaterialTheme.colorScheme.primary.copy(.4f)
                    )
                ) {
                    Icon(
                        imageVector = if (state.data.isArtistFollowed) UnFollowArtistIcon
                        else FollowArtistIcon,
                        contentDescription = null,
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
        }

        item {
            FollowArtist(
                name = state.data.artist.name
            ) {
                navigateToArtistDetail(state.data.artist.id)
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
            FollowArtist(
                name = state.data.artist.name
            ) {
                navigateToArtistDetail(state.data.artist.id)
            }
        }

        item {
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
        }
    }
}

@Composable
private fun FollowArtist(
    name: String,
    onCLick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedButton(
            onClick = onCLick,
            modifier = Modifier.fillMaxWidth(.5f),
            border = BorderStroke(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary.copy(.7f)
            )
        ) {
            Text(
                text = "Explore $name",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        var loadingState by remember {
            mutableStateOf(DataLoadingState.LOADING)
        }

        LaunchedEffect(key1 = Unit) {
            delay(500)
            loadingState = DataLoadingState.LOADED
        }

        ViewArtistScreen(
            state = ViewArtistUiState(
                data = UiArtistData(
                    artist = UiArtist(
                        name = "That Cool Artist"
                    ),
                    listOfSong = (1..10).map {
                        ArtistUiSong(
                            title = "That Cool Song: $it",
                            popularity = it.toLong()
                        )
                    },
                ),
                loadingState = loadingState
            ),
            onEvent = {},
            navigateToArtistDetail = {},
            navigateBack = {}
        )
    }
}
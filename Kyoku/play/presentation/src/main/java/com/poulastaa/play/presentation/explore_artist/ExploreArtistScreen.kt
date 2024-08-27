package com.poulastaa.play.presentation.explore_artist

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.ObserveAsEvent
import com.poulastaa.play.presentation.explore_artist.components.ExploreArtistItem
import com.poulastaa.play.presentation.explore_artist.components.ExploreArtistTopBar

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
        album = viewModel.album.collectAsLazyPagingItems(),
        song = viewModel.song.collectAsLazyPagingItems(),
        onEvent = viewModel::onEvent,
        navigateBack = navigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreArtistScreen(
    modifier: Modifier = Modifier,
    state: ExploreArtistUiState,
    album: LazyPagingItems<ExploreArtistSingleUiData>,
    song: LazyPagingItems<ExploreArtistSingleUiData>,
    onEvent: (ExploreArtistUiEvent) -> Unit,
    navigateBack: () -> Unit
) {
    val scroll = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            ExploreArtistTopBar(
                scroll = scroll,
                header = state.header,
                title = state.artist.name,
                coverImage = state.artist.coverImageUrl,
                isFollowed = state.isFollowed,
                onFollowToggle = {
                    onEvent(ExploreArtistUiEvent.OnFollowToggle)
                },
                navigateBack = navigateBack
            )
        }
    ) { innerPadding ->
        AnimatedContent(
            targetState = song.itemCount == 0 && album.itemCount == 0,
            label = ""
        ) { loadingState ->
            when (loadingState) {
                true -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        content = { CircularProgressIndicator() }
                    )
                }

                false -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                            .padding(innerPadding)
                            .nestedScroll(scroll.nestedScrollConnection),
                        contentPadding = PaddingValues(horizontal = MaterialTheme.dimens.medium1)
                    ) {
                        if (album.itemCount > 0) {
                            item {
                                Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

                                Text(
                                    text = "${stringResource(id = R.string.album)}s",
                                    fontWeight = FontWeight.Medium,
                                    fontSize = MaterialTheme.typography.headlineSmall.fontSize
                                )

                                Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))

                                HorizontalDivider(
                                    thickness = 2.dp,
                                    color = MaterialTheme.colorScheme.primary.copy(.5f)
                                )
                                Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
                            }

                            items(album.itemCount) { index ->
                                album[index]?.let { album ->
                                    ExploreArtistItem(
                                        modifier = Modifier.clickable {
                                            onEvent(ExploreArtistUiEvent.OnAlbumClick(album.id))
                                        },
                                        header = state.header,
                                        item = album,
                                        onThreeDotCLick = {
                                            onEvent(ExploreArtistUiEvent.OnAlbumThreeDotClick(album.id))
                                        }
                                    )
                                }
                            }
                        }

                        if (song.itemCount > 0) {
                            item {
                                Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

                                Text(
                                    text = stringResource(id = R.string.songs),
                                    fontWeight = FontWeight.Medium,
                                    fontSize = MaterialTheme.typography.headlineSmall.fontSize
                                )
                                Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))
                                HorizontalDivider(
                                    thickness = 2.dp,
                                    color = MaterialTheme.colorScheme.primary.copy(.5f)
                                )

                                Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
                            }

                            items(song.itemCount) { index ->
                                song[index]?.let { song ->
                                    ExploreArtistItem(
                                        modifier = Modifier.clickable {
                                            onEvent(ExploreArtistUiEvent.OnSongClick(song.id))
                                        },
                                        header = state.header,
                                        item = song,
                                        onThreeDotCLick = {
                                            onEvent(ExploreArtistUiEvent.OnSongThreeDotClick(song.id))
                                        }
                                    )
                                }
                            }

                            item {
                                Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
                            }
                        }
                    }
                }
            }
        }
    }
}
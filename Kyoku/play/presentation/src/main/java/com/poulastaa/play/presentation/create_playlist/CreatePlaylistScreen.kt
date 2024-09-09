package com.poulastaa.play.presentation.create_playlist

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.poulastaa.core.domain.model.CreatePlaylistType
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.components.AppBackButton
import com.poulastaa.core.presentation.designsystem.components.CompactErrorScreen
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.ObserveAsEvent
import com.poulastaa.play.domain.DataLoadingState
import com.poulastaa.play.presentation.create_playlist.artist.CreatePlaylistArtistRootScreen
import com.poulastaa.play.presentation.create_playlist.artist.CreatePlaylistArtistUiEvent
import com.poulastaa.play.presentation.create_playlist.components.CommonHorizontalPager
import com.poulastaa.play.presentation.create_playlist.components.CreatePlaylistLoadingScreen
import com.poulastaa.play.presentation.create_playlist.components.CreatePlaylistSearchContent
import com.poulastaa.play.presentation.create_playlist.components.CreatePlaylistSearchToBar

@Composable
fun CreatePlaylistRootScreen(
    modifier: Modifier = Modifier,
    playlistId: Long,
    viewModel: CreatePlaylistViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = playlistId) {
        if (viewModel.state.generatedData.isEmpty()) viewModel.init(playlistId)
    }

    ObserveAsEvent(viewModel.uiEvent) {
        when (it) {
            is CreatePlaylistUiAction.EmitToast -> Toast.makeText(
                context,
                it.message.asString(context),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Box(Modifier.fillMaxSize()) {
        CreatePlaylistScreen(
            modifier = modifier,
            state = viewModel.state,
            data = viewModel.pagingData.collectAsLazyPagingItems(),
            onEvent = viewModel::onEvent,
            navigateBack = navigateBack
        )

        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = viewModel.state.artistUiState.isExpanded,
            enter = fadeIn() + expandIn() + slideInHorizontally { it },
            exit = fadeOut() + shrinkOut() + slideOutHorizontally { it }
        ) {
            CreatePlaylistArtistRootScreen(
                modifier = modifier,
                artistId = viewModel.state.artistUiState.id,
                savedSongIdList = viewModel.state.savedSongIdList,
                onEvent = { event ->
                    when (event) {
                        is CreatePlaylistArtistUiEvent.OnAlbumClick -> {
                            viewModel.onEvent(
                                CreatePlaylistUiEvent.OnAlbumClick(
                                    albumId = event.albumId
                                )
                            )
                        }

                        is CreatePlaylistArtistUiEvent.OnSongClick -> {
                            viewModel.onEvent(
                                CreatePlaylistUiEvent.OnSongClick(
                                    type = CreatePlaylistType.SEARCH,
                                    songId = event.songId
                                )
                            )
                        }
                    }
                },
                onBackClick = {
                    viewModel.onEvent(CreatePlaylistUiEvent.OnArtistCancel)
                }
            )
        }

        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = viewModel.state.albumUiState.isExpanded,
            enter = fadeIn() + expandIn() + slideInHorizontally { it },
            exit = fadeOut() + shrinkOut() + slideOutHorizontally { it }
        ) {

        }
    }

    if (viewModel.state.isSearchEnabled ||
        viewModel.state.artistUiState.isExpanded ||
        viewModel.state.albumUiState.isExpanded
    ) BackHandler {
        if (viewModel.state.isSearchEnabled) viewModel.onEvent(CreatePlaylistUiEvent.OnSearchToggle)
        else if (viewModel.state.artistUiState.isExpanded) viewModel.onEvent(CreatePlaylistUiEvent.OnArtistCancel)
        else if (viewModel.state.albumUiState.isExpanded) viewModel.onEvent(CreatePlaylistUiEvent.OnAlbumCancel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreatePlaylistScreen(
    modifier: Modifier = Modifier,
    state: CreatePlaylistUiState,
    data: LazyPagingItems<CreatePlaylistPagingUiData>,
    onEvent: (CreatePlaylistUiEvent) -> Unit,
    navigateBack: () -> Unit
) {
    val horizontalPager = rememberPagerState { state.generatedData.size }
    val focusRequester = remember { FocusRequester() }

    Scaffold(
        topBar = {
            if (state.generatedData.isNotEmpty()) AnimatedContent(
                targetState = horizontalPager.currentPage != horizontalPager.pageCount - 1,
                label = ""
            ) {
                when (it) {
                    true -> {
                        CenterAlignedTopAppBar(
                            title = {
                                Text(
                                    text = state.generatedData[horizontalPager.currentPage].type.value,
                                    fontWeight = FontWeight.SemiBold
                                )
                            },
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = Color.Transparent
                            ),
                            navigationIcon = {
                                AppBackButton {
                                    navigateBack()
                                }
                            }
                        )
                    }

                    false -> {
                        CreatePlaylistSearchToBar(
                            title = stringResource(R.string.search),
                            isSearchEnable = state.isSearchEnabled,
                            searchQuery = state.searchQuery,
                            focusRequester = focusRequester,
                            onEvent = onEvent,
                            navigateBack = navigateBack
                        )
                    }
                }
            }
        },
        modifier = modifier.background(MaterialTheme.colorScheme.surfaceContainer)
    ) { paddingValues ->
        AnimatedContent(
            targetState = state.loadingState,
            label = "",
        ) { loadingState ->
            when (loadingState) {
                DataLoadingState.LOADING -> CreatePlaylistLoadingScreen(
                    navigateBack = navigateBack
                )

                DataLoadingState.LOADED -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                            .padding(paddingValues),
                    ) {
                        HorizontalPager(
                            state = horizontalPager,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                        ) { index ->
                            AnimatedContent(index != horizontalPager.pageCount - 1, label = "") {
                                when (it) {
                                    true -> CommonHorizontalPager(
                                        state = state.generatedData[index],
                                        header = state.header,
                                        onEvent = onEvent
                                    )

                                    false -> CreatePlaylistSearchContent(
                                        header = state.header,
                                        filterType = state.filterType,
                                        data = data,
                                        onEvent = onEvent
                                    )
                                }
                            }
                        }

                        Column {
                            Spacer(Modifier.height(MaterialTheme.dimens.small3))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                PagerIndicator(horizontalPager)
                            }
                        }
                    }
                }

                DataLoadingState.ERROR -> CompactErrorScreen(Modifier.padding(paddingValues))
            }
        }
    }
}

@Composable
fun PagerIndicator(
    pagerState: PagerState
) {
    repeat(pagerState.pageCount) {
        val color = if (it == pagerState.currentPage)
            MaterialTheme.colorScheme.primary.copy(.7f)
        else MaterialTheme.colorScheme.onBackground.copy(.3f)

        Box(
            modifier = Modifier
                .padding(8.dp)
                .clip(CircleShape)
                .background(color = color)
                .size(10.dp)
        )
    }
}
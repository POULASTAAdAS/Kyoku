package com.poulastaa.explore.presentation.search.all_from_artist

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.model.UiPrevArtist
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppErrorExpandedScreen
import com.poulastaa.explore.presentation.components.DummySearch
import com.poulastaa.explore.presentation.components.ExploreExpandedLoadingScreen
import com.poulastaa.explore.presentation.components.ExploreScreenExtendedWrapper
import com.poulastaa.explore.presentation.components.LoadingSongCard
import com.poulastaa.explore.presentation.model.ExploreUiItem
import com.poulastaa.explore.presentation.search.all_from_artist.components.AllFromArtistAlbumCard
import com.poulastaa.explore.presentation.search.all_from_artist.components.AllFromArtistSongCard
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AllFromArtistExpandedScreen(
    modifier: Modifier = Modifier,
    scroll: TopAppBarScrollBehavior,
    state: AllFromArtistUiState,
    song: LazyPagingItems<ExploreUiItem>,
    album: LazyPagingItems<ExploreUiItem>,
    onAction: (AllFromArtistUiAction) -> Unit,
    navigateBack: () -> Unit,
) {
    ExploreScreenExtendedWrapper(
        modifier = modifier,
        scroll = scroll,
        loadingType = state.loadingType,
        isSearchOpen = state.isSearchOpen,
        title = state.artist.name,
        query = state.query.value,
        onQueryChange = {
            onAction(AllFromArtistUiAction.OnSearchQueryChange(it))
        },
        loadingContent = {
            ExploreExpandedLoadingScreen(it, navigateBack) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(.5f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Spacer(Modifier.height(MaterialTheme.dimens.medium1))

                        repeat(10) {
                            LoadingSongCard(
                                Modifier
                                    .fillMaxWidth()
                                    .height(80.dp)
                            )

                            Spacer(Modifier.height(MaterialTheme.dimens.small2))
                        }
                    }

                    Spacer(Modifier.width(MaterialTheme.dimens.small1))

                    Spacer(
                        Modifier
                            .width(MaterialTheme.dimens.small1)
                            .fillMaxHeight()
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                    )

                    Spacer(Modifier.width(MaterialTheme.dimens.small1))

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        Spacer(Modifier.height(MaterialTheme.dimens.medium1))

                        repeat(10) {
                            LoadingSongCard(
                                Modifier
                                    .fillMaxWidth()
                                    .height(80.dp)
                            )

                            Spacer(Modifier.height(MaterialTheme.dimens.small2))
                        }
                    }
                }
            }
        },
        errorContent = { paddingValues, error ->
            AppErrorExpandedScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(brush = Brush.verticalGradient(ThemModeChanger.getGradiantBackground())),
                error = error,
                navigateBack = navigateBack
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .padding(horizontal = MaterialTheme.dimens.medium1),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DummySearch(
                    modifier = Modifier
                        .fillMaxWidth(.7f)
                        .height(38.dp),
                    isSearchOpen = state.isSearchOpen,
                    onToggleSearch = { onAction(AllFromArtistUiAction.OnToggleSearch) },
                )

                Spacer(Modifier.height(MaterialTheme.dimens.medium1))

                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(.5f)
                            .nestedScroll(scroll.nestedScrollConnection)
                    ) {
                        item {
                            Text(
                                text = stringResource(R.string.album),
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        items(album.itemCount) { index ->
                            Spacer(Modifier.height(MaterialTheme.dimens.small3))

                            album[index]?.let { album ->
                                Spacer(Modifier.height(MaterialTheme.dimens.small2))

                                AllFromArtistAlbumCard(album) {
                                    onAction(AllFromArtistUiAction.OnAlbumClick(it))
                                }
                            }
                        }
                    }

                    Spacer(Modifier.width(MaterialTheme.dimens.small1))
                    Spacer(
                        Modifier
                            .fillMaxHeight()
                            .width(MaterialTheme.dimens.small1)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(bottom = MaterialTheme.dimens.medium1)
                    )
                    Spacer(Modifier.width(MaterialTheme.dimens.medium1))

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .nestedScroll(scroll.nestedScrollConnection)
                    ) {
                        item {
                            Text(
                                text = stringResource(R.string.songs),
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        items(song.itemCount) { index ->
                            Spacer(Modifier.height(MaterialTheme.dimens.small3))

                            song[index]?.let { song ->
                                Spacer(Modifier.height(MaterialTheme.dimens.small2))

                                AllFromArtistSongCard(song) {
                                    onAction(AllFromArtistUiAction.OnSongClick(it))
                                }
                            }
                        }
                    }
                }
            }
        },
        navigateBack = {
            when {
                state.isSearchOpen && state.query.value.isNotEmpty() ->
                    onAction(AllFromArtistUiAction.OnSearchQueryClear)

                state.isSearchOpen -> onAction(AllFromArtistUiAction.OnToggleSearch)
                else -> navigateBack()
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    widthDp = 1024,
    heightDp = 540
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 1280,
    heightDp = 740
)
@Composable
private fun Preview() {
    var search by remember { mutableStateOf(false) }

    AppThem {
        Surface {
            AllFromArtistExpandedScreen(
                modifier = Modifier.fillMaxWidth(.7f),
                scroll = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                state = AllFromArtistUiState(
                    loadingType = LoadingType.Loading,
                    artist = UiPrevArtist(
                        name = "That Cool Artist",
                    ),
                    isSearchOpen = search,
                ),
                song = flowOf(PagingData.from((1..10).map {
                    ExploreUiItem(
                        id = it.toLong(),
                        title = "That Cool Song",
                    )
                })).collectAsLazyPagingItems(),
                album = flowOf(PagingData.from((1..5).map {
                    ExploreUiItem(
                        id = it.toLong(),
                        title = "That Cool Album",
                    )
                })).collectAsLazyPagingItems(),
                onAction = {
                    if (it == AllFromArtistUiAction.OnToggleSearch) search = !search
                },
                navigateBack = {}
            )
        }
    }
}
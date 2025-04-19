package com.poulastaa.explore.presentation.search.all_from_artist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.columnPagingLoadingLoadState
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.model.UiPrevArtist
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppErrorScreen
import com.poulastaa.core.presentation.ui.components.AppFilterChip
import com.poulastaa.explore.presentation.components.ExploreCompactLoadingScreen
import com.poulastaa.explore.presentation.components.ExploreScreenWrapper
import com.poulastaa.explore.presentation.components.LoadingSongCard
import com.poulastaa.explore.presentation.model.ExploreUiItem
import com.poulastaa.explore.presentation.search.all_from_artist.components.AllFromArtistAlbumCard
import com.poulastaa.explore.presentation.search.all_from_artist.components.AllFromArtistSongCard
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AllFromArtistCompactScreen(
    modifier: Modifier = Modifier,
    scroll: TopAppBarScrollBehavior,
    state: AllFromArtistUiState,
    song: LazyPagingItems<ExploreUiItem>,
    album: LazyPagingItems<ExploreUiItem>,
    onAction: (AllFromArtistUiAction) -> Unit,
    navigateBack: () -> Unit,
) {
    ExploreScreenWrapper(
        modifier = modifier,
        scroll = scroll,
        loadingType = state.loadingType,
        isSearchOpen = state.isSearchOpen,
        title = state.artist.name,
        query = state.query.value,
        onQueryChange = {
            onAction(AllFromArtistUiAction.OnSearchQueryChange(it))
        },
        onToggleSearch = {
            onAction(AllFromArtistUiAction.OnToggleSearch)
        },
        loadingContent = {
            ExploreCompactLoadingScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            ThemModeChanger.getGradiantBackground()
                        )
                    )
                    .padding(it)
                    .padding(horizontal = MaterialTheme.dimens.medium1),
                title = stringResource(R.string.that_cool_artist),
                navigateBack = navigateBack
            )
        },
        errorContent = { paddingValues, error ->
            AppErrorScreen(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(ThemModeChanger.getGradiantBackground())
                    )
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(MaterialTheme.dimens.medium1),
                error = error,
                navigateBack = navigateBack
            )
        },
        content = {
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
                ) {
                    AppFilterChip(
                        title = FilterType.ALL.name.lowercase(),
                        isSelected = state.filterType == FilterType.ALL,
                        onClick = {
                            onAction(AllFromArtistUiAction.OnFilterChange(FilterType.ALL))
                        }
                    )

                    AppFilterChip(
                        title = FilterType.ALBUM.name.lowercase(),
                        isSelected = state.filterType == FilterType.ALBUM,
                        onClick = {
                            onAction(AllFromArtistUiAction.OnFilterChange(FilterType.ALBUM))
                        }
                    )

                    AppFilterChip(
                        title = FilterType.SONG.name.lowercase(),
                        isSelected = state.filterType == FilterType.SONG,
                        onClick = {
                            onAction(AllFromArtistUiAction.OnFilterChange(FilterType.SONG))
                        }
                    )
                }
            }

            if ((state.filterType == FilterType.ALBUM || state.filterType == FilterType.ALL) && album.itemCount > 0) {
                item {
                    Spacer(Modifier.height(MaterialTheme.dimens.medium1))
                }

                item {
                    Card(
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(
                            contentColor = MaterialTheme.colorScheme.primary,
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 5.dp
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.album),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = MaterialTheme.dimens.small3)
                                .padding(horizontal = MaterialTheme.dimens.medium1),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
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

            if ((state.filterType == FilterType.SONG || state.filterType == FilterType.ALL) && song.itemCount > 0) {
                item {
                    Spacer(Modifier.height(MaterialTheme.dimens.medium1))
                }

                item {
                    Card(
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(
                            contentColor = MaterialTheme.colorScheme.primary,
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 5.dp
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.songs),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = MaterialTheme.dimens.small3)
                                .padding(horizontal = MaterialTheme.dimens.medium1),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                items(song.itemCount) { index ->
                    Spacer(Modifier.height(MaterialTheme.dimens.small2))

                    song[index]?.let { song ->
                        Spacer(Modifier.height(MaterialTheme.dimens.small2))

                        AllFromArtistSongCard(song) {
                            onAction(AllFromArtistUiAction.OnSongClick(it))
                        }
                    }
                }
            }

            columnPagingLoadingLoadState(
                data = song.loadState,
                retry = {
                    song.retry()
                }
            ) {
                item {
                    Spacer(Modifier.height(MaterialTheme.dimens.small2))
                }

                item {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        repeat(3) {
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
        navigateBack = {
            when {
                state.isSearchOpen && state.query.value.isNotEmpty() ->
                    onAction(AllFromArtistUiAction.OnSearchQueryClear)

                state.isSearchOpen -> onAction(AllFromArtistUiAction.OnToggleSearch)
                else -> navigateBack()
            }
        },
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun Preview() {
    var search by remember { mutableStateOf(false) }

    AppThem {
        Surface {
            AllFromArtistCompactScreen(
                scroll = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                state = AllFromArtistUiState(
                    loadingType = LoadingType.Content,
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
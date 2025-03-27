package com.poulastaa.explore.presentation.search.all_from_artist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import com.poulastaa.core.presentation.designsystem.CacheImageReq
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.model.UiPrevArtist
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.ArrowBackIcon
import com.poulastaa.core.presentation.designsystem.ui.FilterAlbumIcon
import com.poulastaa.core.presentation.designsystem.ui.SearchIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppErrorScreen
import com.poulastaa.core.presentation.ui.components.AppFilterChip
import com.poulastaa.explore.presentation.search.all_from_artist.components.AllFromArtistCompactLoadingScreen
import com.poulastaa.explore.presentation.search.all_from_artist.components.AllFromArtistTopBar
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AllFromArtistCompactScreen(
    modifier: Modifier = Modifier,
    scroll: TopAppBarScrollBehavior,
    state: AllFromArtistUiState,
    song: LazyPagingItems<AllFromArtistUiItem>,
    album: LazyPagingItems<AllFromArtistUiItem>,
    onAction: (AllFromArtistUiAction) -> Unit,
    navigateBack: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            if (state.loadingType is LoadingType.Content) AllFromArtistTopBar(
                modifier = modifier,
                scroll = scroll,
                artist = state.artist.name,
                isSearchOpen = state.isSearchOpen,
                query = state.query.value,
                onQueryChange = { onAction(AllFromArtistUiAction.OnSearchQueryChange(it)) },
                navigateBack = {
                    when {
                        state.isSearchOpen &&
                                state.query.value.isNotEmpty() -> onAction(AllFromArtistUiAction.OnSearchQueryClear)

                        state.isSearchOpen -> onAction(AllFromArtistUiAction.OnToggleSearch)
                        else -> navigateBack()
                    }
                },
                onSearch = {
                    focusManager.clearFocus()
                }
            )
        }
    ) { paddingValues ->
        when (state.loadingType) {
            LoadingType.Loading -> AllFromArtistCompactLoadingScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            ThemModeChanger.getGradiantBackground()
                        )
                    )
                    .padding(paddingValues)
                    .padding(horizontal = MaterialTheme.dimens.medium1),
                navigateBack = navigateBack
            )

            LoadingType.Content -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            ThemModeChanger.getGradiantBackground()
                        )
                    )
                    .padding(paddingValues)
                    .nestedScroll(scroll.nestedScrollConnection),
                contentPadding = PaddingValues(MaterialTheme.dimens.medium1)
            ) {
                item {
                    AnimatedVisibility(
                        state.isSearchOpen.not(),
                        enter = fadeIn(tween(400)) + slideInVertically(tween(400)),
                        exit = fadeOut(tween(400)) + slideOutVertically(tween(400))
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(38.dp),
                            colors = CardDefaults.cardColors(
                                contentColor = MaterialTheme.colorScheme.primary,
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 5.dp
                            ),
                            onClick = {
                                onAction(AllFromArtistUiAction.OnToggleSearch)
                            },
                            shape = CircleShape
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = MaterialTheme.dimens.small1),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = SearchIcon,
                                    contentDescription = null
                                )

                                Spacer(Modifier.width(MaterialTheme.dimens.small1))

                                Text(
                                    text = stringResource(R.string.search),
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(Modifier.height(MaterialTheme.dimens.medium1))
                }

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
                }

                items(album.itemCount) { index ->
                    Spacer(Modifier.height(MaterialTheme.dimens.small3))

                    album[index]?.let { album ->
                        Spacer(Modifier.height(MaterialTheme.dimens.small2))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Card(
                                modifier = Modifier.aspectRatio(1f),
                                shape = MaterialTheme.shapes.extraSmall,
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            ) {
                                SubcomposeAsyncImage(
                                    model = CacheImageReq.imageReq(
                                        album.poster,
                                        LocalContext.current
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.FillBounds,
                                    loading = {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.fillMaxSize(.4f),
                                                strokeWidth = 2.dp,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    },
                                    error = {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = FilterAlbumIcon,
                                                contentDescription = null,
                                                modifier = Modifier.fillMaxSize(.7f),
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                )
                            }

                            Spacer(Modifier.width(MaterialTheme.dimens.medium1))

                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(.8f),
                                verticalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Text(
                                    text = album.title,
                                    fontWeight = FontWeight.Medium
                                )

                                Text(
                                    text = album.artist,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                                )
                            }

                            Spacer(Modifier.weight(1f))

                            IconButton(
                                onClick = {
                                    onAction(AllFromArtistUiAction.OnAlbumClick(album.id))
                                }
                            ) {
                                Icon(
                                    imageVector = ArrowBackIcon,
                                    contentDescription = null,
                                    modifier = Modifier.rotate(180f)
                                )
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
                }

                items(song.itemCount) { index ->
                    Spacer(Modifier.height(MaterialTheme.dimens.small3))

                    song[index]?.let { song ->
                        Spacer(Modifier.height(MaterialTheme.dimens.small2))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Card(
                                modifier = Modifier.aspectRatio(1f),
                                shape = MaterialTheme.shapes.extraSmall,
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            ) {
                                SubcomposeAsyncImage(
                                    model = CacheImageReq.imageReq(
                                        song.poster,
                                        LocalContext.current
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.FillBounds,
                                    loading = {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.fillMaxSize(.4f),
                                                strokeWidth = 2.dp,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    },
                                    error = {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = FilterAlbumIcon,
                                                contentDescription = null,
                                                modifier = Modifier.fillMaxSize(.7f),
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                )
                            }

                            Spacer(Modifier.width(MaterialTheme.dimens.medium1))

                            Column(
                                modifier = Modifier
                                    .fillMaxHeight(),
                                verticalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Text(
                                    text = song.title,
                                    fontWeight = FontWeight.Medium
                                )

                                Text(
                                    text = song.artist,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                                )
                            }
                        }
                    }
                }
            }

            is LoadingType.Error -> AppErrorScreen(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(ThemModeChanger.getGradiantBackground())
                    )
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(MaterialTheme.dimens.medium1),
                error = state.loadingType,
                navigateBack = navigateBack
            )
        }
    }
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
                    AllFromArtistUiItem(
                        id = it.toLong(),
                        title = "That Cool Song",
                        artist = (1..3).joinToString(",") { "Artist $it" }
                    )
                })).collectAsLazyPagingItems(),
                album = flowOf(PagingData.from((1..5).map {
                    AllFromArtistUiItem(
                        id = it.toLong(),
                        title = "That Cool Album",
                        artist = (1..3).joinToString(",") { "Artist $it" }
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
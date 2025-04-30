package com.poulastaa.add.presentation.album

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import com.poulastaa.add.presentation.album.components.AddAlbumSavedButton
import com.poulastaa.add.presentation.album.components.AddAlbumSearchFilterRow
import com.poulastaa.add.presentation.album.view_album.AddAlbumFloatingActionButton
import com.poulastaa.add.presentation.album.view_album.AddAlbumViewRootScreen
import com.poulastaa.add.presentation.artist.AddFilterLoadingSearchTopBar
import com.poulastaa.add.presentation.artist.components.AddArtistLoadingFilterCard
import com.poulastaa.add.presentation.components.AddSearchTopBar
import com.poulastaa.core.presentation.designsystem.CacheImageReq
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.gridPagingLoadingContent
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.shimmerEffect
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.ArrowDownIcon
import com.poulastaa.core.presentation.designsystem.ui.CheckIcon
import com.poulastaa.core.presentation.designsystem.ui.FilterAlbumIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppErrorScreen
import kotlinx.coroutines.flow.flowOf
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddAlbumExtendedScreen(
    isExtendedSearch: Boolean = false,
    grids: Int = 6,
    scroll: TopAppBarScrollBehavior,
    state: AddAlbumUiState,
    album: LazyPagingItems<UiAlbum>,
    onAction: (AddAlbumUiAction) -> Unit,
    navigateBack: () -> Unit,
) {
    Row {
        Scaffold(
            topBar = {
                if (state.loadingType == LoadingType.Content) AddSearchTopBar(
                    scrollBehavior = scroll,
                    label = stringResource(R.string.search_album),
                    query = state.query.value,
                    isExtended = isExtendedSearch,
                    focusManager = LocalFocusManager.current,
                    filterTypeContent = {
                        AddAlbumSearchFilterRow(
                            searchFilterType = state.searchFilterType,
                            onAction = onAction
                        )
                    },
                    actions = {
                        AddAlbumSavedButton(
                            isEditEnabled = state.isEditEnabled,
                            isSavingAlbums = state.isSavingAlbums,
                            onAction = onAction
                        )
                    },
                    onValueChange = { onAction(AddAlbumUiAction.OnSearchQueryChange(it)) },
                    navigateBack = {
                        if (state.query.value.isNotEmpty())
                            onAction(AddAlbumUiAction.OnSearchQueryChange(""))
                        else if (state.isEditEnabled) onAction(AddAlbumUiAction.OnClearAllDialogToggle)
                        else navigateBack()
                    }
                )
            },
            floatingActionButton = {
                AddAlbumFloatingActionButton(
                    size = state.selectedAlbums.size,
                    isEditEnabled = state.isEditEnabled,
                    onAction = onAction
                )
            },
            modifier = Modifier
                .animateContentSize(tween(600))
                .fillMaxHeight()
                .fillMaxWidth(if (state.viewAlbumScreenState.isVisible) .65f else 1f)
        ) {
            when (state.loadingType) {
                LoadingType.Loading -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = ThemModeChanger.getGradiantBackground()
                            )
                        )
                        .verticalScroll(rememberScrollState())
                        .padding(it)
                        .padding(MaterialTheme.dimens.medium1),
                ) {
                    AddFilterLoadingSearchTopBar(
                        filterItemCount = 2,
                        isExtendedSearch = isExtendedSearch,
                        navigateBack = navigateBack
                    )

                    Spacer(Modifier.height(MaterialTheme.dimens.medium1))

                    if (isExtendedSearch.not() && state.viewAlbumScreenState.isVisible) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2)
                        ) {
                            repeat(2) {
                                AddArtistLoadingFilterCard()
                            }
                        }

                        Spacer(Modifier.height(MaterialTheme.dimens.medium1))
                    }

                    repeat(8) {
                        LoadingAlbumCardRow(grids)

                        Spacer(Modifier.height(MaterialTheme.dimens.medium1))
                    }
                }

                LoadingType.Content -> LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = ThemModeChanger.getGradiantBackground()
                            )
                        )
                        .nestedScroll(scroll.nestedScrollConnection)
                        .padding(it),
                    columns = GridCells.Fixed(grids),
                    contentPadding = PaddingValues(MaterialTheme.dimens.medium1),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2)
                ) {
                    if (isExtendedSearch.not()) item(
                        span = { GridItemSpan(maxLineSpan) }
                    ) {
                        AddAlbumSearchFilterRow(
                            searchFilterType = state.searchFilterType,
                            onAction = onAction
                        )
                    }

                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Spacer(Modifier.height(MaterialTheme.dimens.small3))
                    }

                    items(album.itemCount) { index ->
                        album[index]?.let { item ->
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize(.85f)
                                        .background(MaterialTheme.colorScheme.primaryContainer)
                                        .clip(MaterialTheme.shapes.extraSmall)
                                        .clickable {
                                            onAction(
                                                AddAlbumUiAction.OnAlbumClick(
                                                    album = item,
                                                    clickType = AddAlbumUiAction.ClickType.EDIT
                                                )
                                            )
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Card(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .aspectRatio(1f)
                                            .blur(3.dp),
                                        shape = MaterialTheme.shapes.extraSmall,
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.primaryContainer
                                        ),
                                        elevation = CardDefaults.cardElevation(
                                            defaultElevation = 5.dp,
                                            pressedElevation = 0.dp
                                        )
                                    ) {
                                        SubcomposeAsyncImage(
                                            model = CacheImageReq.imageReq(
                                                item.poster,
                                                LocalContext.current
                                            ),
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.FillBounds,
                                            error = {
                                                Box(
                                                    modifier = Modifier.fillMaxSize(),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        imageVector = FilterAlbumIcon,
                                                        contentDescription = null,
                                                        modifier = Modifier.fillMaxSize(.7f),
                                                        tint = MaterialTheme.colorScheme.background
                                                    )
                                                }
                                            }
                                        )
                                    }

                                    Card(
                                        modifier = Modifier
                                            .animateContentSize(tween(600))
                                            .align(Alignment.Center)
                                            .fillMaxSize(if (item.isSelected) .65f else 1f)
                                            .aspectRatio(1f),
                                        shape = MaterialTheme.shapes.extraSmall,
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.primary
                                        ),
                                        elevation = CardDefaults.cardElevation(
                                            defaultElevation = 5.dp,
                                            pressedElevation = 0.dp
                                        )
                                    ) {
                                        SubcomposeAsyncImage(
                                            model = CacheImageReq.imageReq(
                                                item.poster,
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
                                                        color = MaterialTheme.colorScheme.background
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
                                                        tint = MaterialTheme.colorScheme.background
                                                    )
                                                }
                                            }
                                        )
                                    }

                                    this@Column.AnimatedVisibility(
                                        visible = item.isSelected,
                                        modifier = Modifier
                                            .padding(MaterialTheme.dimens.small2)
                                            .align(Alignment.TopStart),
                                        enter = fadeIn(tween(600)),
                                        exit = fadeOut(tween(600)),
                                    ) {
                                        Card(
                                            modifier = Modifier
                                                .size(20.dp),
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.secondary,
                                                contentColor = MaterialTheme.colorScheme.primary
                                            ),
                                            shape = CircleShape,
                                            elevation = CardDefaults.cardElevation(
                                                defaultElevation = 3.dp
                                            )
                                        ) {
                                            Icon(
                                                imageVector = CheckIcon,
                                                contentDescription = null,
                                                modifier = Modifier.padding(1.dp)
                                            )
                                        }
                                    }

                                    IconButton(
                                        modifier = Modifier
                                            .padding(MaterialTheme.dimens.small2)
                                            .align(Alignment.TopEnd),
                                        onClick = {
                                            onAction(
                                                AddAlbumUiAction.OnAlbumClick(
                                                    album = item,
                                                    clickType = AddAlbumUiAction.ClickType.VIEW
                                                )
                                            )
                                        },
                                        colors = IconButtonDefaults.iconButtonColors(
                                            contentColor = MaterialTheme.colorScheme.primary,
                                            containerColor = MaterialTheme.colorScheme.background
                                        ),
                                    ) {
                                        Icon(
                                            imageVector = ArrowDownIcon,
                                            contentDescription = null,
                                            modifier = Modifier.rotate(-90f)
                                        )
                                    }
                                }

                                Spacer(Modifier.weight(1f))

                                Text(
                                    text = item.name,
                                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Spacer(Modifier.height(MaterialTheme.dimens.small2))
                    }

                    gridPagingLoadingContent(
                        gridSize = grids,
                        data = album.loadState,
                        retry = {
                            album.retry()
                        }
                    ) {
                        LoadingAlbumCardRow(grids)
                    }
                }

                is LoadingType.Error -> AppErrorScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = ThemModeChanger.getGradiantBackground()
                            )
                        )
                        .padding(it)
                        .padding(MaterialTheme.dimens.medium1),
                    error = state.loadingType,
                    navigateBack = navigateBack
                )

            }
        }

        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = state.viewAlbumScreenState.isVisible,
            enter = fadeIn(tween(600)) + slideInHorizontally(tween(600), initialOffsetX = { it }),
            exit = fadeOut(tween(600)) + slideOutHorizontally(tween(600), targetOffsetX = { it }),
        ) {
            AddAlbumViewRootScreen(
                album = state.viewAlbumScreenState.album,
                navigateBack = {
                    onAction(AddAlbumUiAction.OnViewCancel)
                }
            )
        }
    }
}

@Composable
private fun LoadingAlbumCardRow(columns: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        repeat(columns) {
            Column(
                modifier = Modifier
                    .aspectRatio(1f)
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier.fillMaxSize(.85f),
                    shape = MaterialTheme.shapes.extraSmall,
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 5.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .shimmerEffect(MaterialTheme.colorScheme.primary)
                    )
                }

                Spacer(Modifier.height(MaterialTheme.dimens.small2))

                Card(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(.7f),
                    shape = MaterialTheme.shapes.extraSmall,
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 5.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .shimmerEffect(MaterialTheme.colorScheme.primary)
                    )
                }
            }
        }
    }
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
    AppThem {
        Surface {
            AddAlbumExtendedScreen(
                isExtendedSearch = isSystemInDarkTheme(),
                scroll = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                state = AddAlbumUiState(
                    loadingType = LoadingType.Content,
                    selectedAlbums = (1..5).map {
                        UiAlbum(
                            name = "That Cool Album",
                            artist = "That Cool Artist",
                            releaseYear = 2025,
                            isSelected = Random.nextBoolean()
                        )
                    }
                ),
                album = flowOf(
                    PagingData.from((1..10).map {
                        UiAlbum(
                            name = "That Cool Album",
                            artist = "That Cool Artist",
                            releaseYear = 2025,
                            isSelected = Random.nextBoolean()
                        )
                    })
                ).collectAsLazyPagingItems(),
                onAction = {},
                navigateBack = {}
            )
        }
    }
}
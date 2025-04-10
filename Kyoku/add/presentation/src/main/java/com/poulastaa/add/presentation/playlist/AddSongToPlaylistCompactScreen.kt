package com.poulastaa.add.presentation.playlist

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.poulastaa.add.presentation.playlist.components.AddSongToPlaylistLoadingTopBar
import com.poulastaa.add.presentation.playlist.components.CreatePlaylistItemCard
import com.poulastaa.add.presentation.playlist.components.LoadingNavigationBall
import com.poulastaa.add.presentation.playlist.components.LoadingSongCard
import com.poulastaa.add.presentation.playlist.components.PagerIndicator
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.CloseIcon
import com.poulastaa.core.presentation.designsystem.ui.SearchIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppErrorScreen
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddSongToPlaylistCompactScreen(
    state: AddSongToPlaylistUiState,
    searchData: LazyPagingItems<AddToPlaylistUiItem>,
    onAction: (AddSongToPlaylistUiAction) -> Unit,
    navigateBack: () -> Unit,
) {
    val haptic = LocalHapticFeedback.current
    val horizontalPager = rememberPagerState { state.staticData.size + 1 }
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            when (state.loadingType) {
                is LoadingType.Loading -> AddSongToPlaylistLoadingTopBar(navigateBack)

                is LoadingType.Content -> if (horizontalPager.currentPage > state.staticData.size - 1) TopAppBar(
                    title = {
                        OutlinedTextField(
                            value = state.query,
                            onValueChange = {
                                onAction(AddSongToPlaylistUiAction.OnSearchQueryChange(it))
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = CircleShape,
                            label = {
                                Text(
                                    text = stringResource(R.string.search_anything),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = SearchIcon,
                                    contentDescription = null,
                                    modifier = Modifier.clickable {
                                        focusManager.clearFocus()
                                    }
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.primary,
                                unfocusedLabelColor = MaterialTheme.colorScheme.primaryContainer,
                                focusedLabelColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedTrailingIconColor = MaterialTheme.colorScheme.primaryContainer,
                                focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                            ),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    focusManager.clearFocus()
                                }
                            ),
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.None,
                                autoCorrectEnabled = false,
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Search,
                                showKeyboardOnFocus = true,
                            )
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                if (state.query.isNotEmpty()) {
                                    onAction(AddSongToPlaylistUiAction.OnSearchQueryChange(""))
                                } else navigateBack()
                            }
                        ) {
                            Icon(
                                imageVector = CloseIcon,
                                contentDescription = null
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Transparent,
                        navigationIconContentColor = MaterialTheme.colorScheme.primary
                    ),
                ) else CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(state.staticData[horizontalPager.currentPage].type.value),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.animateContentSize(tween(400))
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = navigateBack,
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(
                                imageVector = CloseIcon,
                                contentDescription = null
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent,
                        navigationIconContentColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.primary
                    )
                )

                else -> Unit
            }
        }
    ) {
        when (state.loadingType) {
            LoadingType.Loading -> Column(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(ThemModeChanger.getGradiantBackground())
                    )
                    .fillMaxSize()
                    .padding(it)
                    .padding(MaterialTheme.dimens.medium1)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(.95f)
                        .verticalScroll(rememberScrollState()),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    )
                ) {
                    Spacer(Modifier.height(MaterialTheme.dimens.medium1))

                    repeat(10) {
                        LoadingSongCard()
                        Spacer(Modifier.height(MaterialTheme.dimens.small1))
                    }

                    Spacer(Modifier.height(MaterialTheme.dimens.medium1))
                }

                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LoadingNavigationBall(MaterialTheme.colorScheme.background)
                    Spacer(Modifier.width(MaterialTheme.dimens.small2))

                    repeat(3) {
                        LoadingNavigationBall()
                        Spacer(Modifier.width(MaterialTheme.dimens.small2))
                    }
                }
            }

            is LoadingType.Error -> AppErrorScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(MaterialTheme.dimens.medium1),
                error = state.loadingType,
                navigateBack = navigateBack
            )

            LoadingType.Content -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(ThemModeChanger.getGradiantBackground())
                    )
            ) {
                HorizontalPager(
                    state = horizontalPager,
                    modifier = Modifier
                        .padding(it)
                        .fillMaxWidth()
                        .fillMaxHeight(.95f)
                ) { pageIndex ->
                    Card(
                        modifier = Modifier
                            .padding(MaterialTheme.dimens.medium1)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 5.dp
                        )
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(MaterialTheme.dimens.medium1)
                        ) {
                            if (pageIndex < state.staticData.size) items(state.staticData[pageIndex].data) { item ->
                                CreatePlaylistItemCard(item, onAction, haptic)
                            } else items(searchData.itemCount) { index ->
                                searchData[index]?.let {
                                    CreatePlaylistItemCard(it, onAction, haptic)
                                }
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PagerIndicator(horizontalPager)
                }
            }
        }
    }
}


internal val PREV_DATA = flowOf(
    PagingData.from(
        ((1..5).map {
            AddToPlaylistUiItem(
                id = it.toLong(),
                title = "That Cool Song",
                type = AddToPlaylistItemUiType.SONG
            )
        } + (1..5).map {
            AddToPlaylistUiItem(
                id = it.toLong(),
                title = "That Cool Artist",
                type = AddToPlaylistItemUiType.ARTIST
            )
        } + (1..5).map {
            AddToPlaylistUiItem(
                id = it.toLong(),
                title = "That Cool Playlist",
                type = AddToPlaylistItemUiType.PLAYLIST
            )
        } + (1..5).map {
            AddToPlaylistUiItem(
                id = it.toLong(),
                title = "That Cool Artist",
                type = AddToPlaylistItemUiType.ALBUM
            )
        }).shuffled()
    )
)

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            AddSongToPlaylistCompactScreen(
                state = AddSongToPlaylistUiState(
                    loadingType = LoadingType.Content,
                    staticData = listOf(
                        AddSongToPlaylistPageUiItem(
                            type = AddSongToPlaylistPageUiType.YOUR_FAVOURITES,
                            data = (1..10).map {
                                AddToPlaylistUiItem(
                                    title = "That Cool Song",
                                    artist = "That Cool Artist",
                                    type = AddToPlaylistItemUiType.SONG
                                )
                            }
                        ),
                        AddSongToPlaylistPageUiItem(
                            type = AddSongToPlaylistPageUiType.SUGGESTED_FOR_YOU,
                            data = (1..10).map {
                                AddToPlaylistUiItem(
                                    title = "That Cool Song",
                                    artist = "That Cool Artist",
                                    type = AddToPlaylistItemUiType.SONG
                                )
                            }
                        ),
                        AddSongToPlaylistPageUiItem(
                            type = AddSongToPlaylistPageUiType.YOU_MAY_ALSO_LIKE,
                            data = (1..10).map {
                                AddToPlaylistUiItem(
                                    title = "That Cool Song",
                                    artist = "That Cool Artist",
                                    type = AddToPlaylistItemUiType.SONG
                                )
                            }
                        )
                    )
                ),
                searchData = PREV_DATA.collectAsLazyPagingItems(),
                onAction = {},
                navigateBack = {}
            )
        }
    }
}
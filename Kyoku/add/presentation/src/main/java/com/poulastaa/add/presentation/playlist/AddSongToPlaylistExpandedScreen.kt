package com.poulastaa.add.presentation.playlist

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.poulastaa.add.presentation.playlist.components.AddSongToPlaylistLoadingContent
import com.poulastaa.add.presentation.playlist.components.AddSongToPlaylistLoadingTopBar
import com.poulastaa.add.presentation.playlist.components.LoadingSongCard
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.CloseIcon
import com.poulastaa.core.presentation.designsystem.ui.SearchIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppErrorExpandedScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddSongToPlaylistExpandedScreen(
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
                LoadingType.Content -> if (horizontalPager.currentPage > state.staticData.size - 1) TopAppBar(
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

                LoadingType.Loading -> AddSongToPlaylistLoadingTopBar(
                    titleWidth = .3f,
                    navigateBack
                )

                is LoadingType.Error -> Unit
            }
        },
        content = {
            when (state.loadingType) {
                LoadingType.Loading -> AddSongToPlaylistLoadingContent(it) {
                    Row {
                        repeat(2) {
                            LoadingSongCard(
                                Modifier
                                    .weight(1f)
                                    .height(80.dp)
                                    .padding(horizontal = MaterialTheme.dimens.medium1)
                            )
                        }
                    }
                }

                is LoadingType.Error -> AppErrorExpandedScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                        .padding(MaterialTheme.dimens.medium1),
                    error = state.loadingType,
                    navigateBack = navigateBack
                )

                LoadingType.Content -> TODO()
            }
        }
    )
}


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
            AddSongToPlaylistExpandedScreen(
                state = AddSongToPlaylistUiState(
                    loadingType = LoadingType.Loading,
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
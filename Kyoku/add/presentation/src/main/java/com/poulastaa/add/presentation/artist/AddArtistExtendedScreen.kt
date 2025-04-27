package com.poulastaa.add.presentation.artist

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.poulastaa.add.presentation.artist.components.AddArtistCard
import com.poulastaa.add.presentation.artist.components.AddArtistLoadingFilterCard
import com.poulastaa.add.presentation.artist.components.AddArtistSearchFilterChips
import com.poulastaa.add.presentation.artist.components.LoadingArtistCard
import com.poulastaa.add.presentation.components.AddSearchTopBar
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.CloseIcon
import com.poulastaa.core.presentation.designsystem.ui.SearchIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppErrorExpandedScreen
import kotlinx.coroutines.flow.flowOf
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddArtistExtendedScreen(
    scroll: TopAppBarScrollBehavior,
    columns: Int,
    isExtendedSearch: Boolean = false,
    state: AddArtistUiState,
    artist: LazyPagingItems<UiArtist>,
    onAction: (AddArtistUiAction) -> Unit,
    navigateBack: () -> Unit,
) {
    val haptic = LocalHapticFeedback.current

    Scaffold(
        topBar = {
            if (state.loadingType == LoadingType.Content) AddSearchTopBar(
                scrollBehavior = scroll,
                label = stringResource(R.string.search_artist),
                query = state.query,
                isExtended = isExtendedSearch,
                focusManager = LocalFocusManager.current,
                onValueChange = { onAction(AddArtistUiAction.OnSearchQueryChange(it)) },
                filterTypeContent = {
                    AddArtistSearchFilterChips(
                        searchFilterType = state.searchFilterType,
                        onAction = onAction
                    )
                },
                actions = {
                    AddArtistActions(
                        isSaving = state.isSaving,
                        isEditEnabled = state.isEditEnabled,
                        onAction = onAction,
                        haptic = haptic
                    )
                },
                navigateBack = {
                    if (state.query.isEmpty()) navigateBack()
                    else if (state.isEditEnabled) onAction(AddArtistUiAction.OnCancelSaveClick)
                    else onAction(AddArtistUiAction.OnSearchQueryChange(""))

                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                }
            )
        },
        content = {
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
                        .padding(MaterialTheme.dimens.medium1)
                ) {
                    AddFilterLoadingSearchTopBar(
                        filterItemCount = 2,
                        isExtendedSearch = isExtendedSearch,
                        navigateBack = navigateBack
                    )

                    Spacer(Modifier.width(MaterialTheme.dimens.medium1))

                    if (isExtendedSearch.not()) Row(
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
                    ) {
                        repeat(2) {
                            AddArtistLoadingFilterCard()
                        }
                    }

                    Spacer(Modifier.height(MaterialTheme.dimens.medium1))

                    repeat(5) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            repeat(columns) {
                                LoadingArtistCard(
                                    Modifier
                                        .aspectRatio(1f)
                                        .weight(1f)
                                )
                            }
                        }

                        Spacer(Modifier.height(MaterialTheme.dimens.medium1))
                    }
                }

                is LoadingType.Error -> AppErrorExpandedScreen(
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
                    columns = GridCells.Fixed(columns),
                    contentPadding = PaddingValues(MaterialTheme.dimens.medium1)
                ) {
                    if (isExtendedSearch.not()) item(span = { GridItemSpan((maxLineSpan)) }) {
                        AddArtistSearchFilterChips(
                            Modifier.fillMaxWidth(),
                            state.searchFilterType,
                            onAction
                        )
                    }

                    item(span = { GridItemSpan((maxLineSpan)) }) {
                        Spacer(Modifier.height(MaterialTheme.dimens.medium1))
                    }

                    items(artist.itemCount) { index ->
                        artist[index]?.let { item ->
                            AddArtistCard(
                                modifier = Modifier.fillMaxWidth(),
                                item = item,
                                haptic = haptic,
                                onAction = onAction
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
internal fun AddFilterLoadingSearchTopBar(
    filterItemCount: Int,
    isExtendedSearch: Boolean,
    navigateBack: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = navigateBack
        ) {
            Icon(
                imageVector = CloseIcon,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(.8f)
            )
        }

        Spacer(Modifier.width(MaterialTheme.dimens.small3))

        if (isExtendedSearch) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
            ) {
                repeat(filterItemCount) {
                    AddArtistLoadingFilterCard()
                }
            }

            Spacer(Modifier.width(MaterialTheme.dimens.medium1))
        }

        Spacer(Modifier.weight(1f))

        Card(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(if (isExtendedSearch) 1f else .7f),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent,
            ),
            border = BorderStroke(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary
            ),
            shape = CircleShape
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = MaterialTheme.dimens.medium1),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(.8f),
                    text = "${stringResource(R.string.search)} ${stringResource(R.string.artist).lowercase()}",
                    color = MaterialTheme.colorScheme.primary.copy(.8f),
                )

                Spacer(Modifier.weight(1f))

                IconButton(
                    onClick = navigateBack
                ) {
                    Icon(
                        imageVector = SearchIcon,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(.8f)
                    )
                }
            }
        }

        Spacer(Modifier.weight(1f))
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
            AddArtistExtendedScreen(
                scroll = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                columns = 6,
                isExtendedSearch = isSystemInDarkTheme(),
                state = AddArtistUiState(
                    loadingType = LoadingType.Content,
                    selectedArtist = (1..5).map {
                        UiArtist(name = "That Cool Artist")
                    },
                ),
                artist = flowOf(
                    PagingData.from(
                        (1..40).map {
                            UiArtist(
                                name = "That Cool Artist",
                                isSelected = Random.nextBoolean()
                            )
                        }
                    )
                ).collectAsLazyPagingItems(),
                onAction = {},
                navigateBack = {}
            )
        }
    }
}
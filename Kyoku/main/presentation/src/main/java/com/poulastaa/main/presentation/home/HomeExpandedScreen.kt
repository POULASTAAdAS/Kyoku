package com.poulastaa.main.presentation.home

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ThemChanger
import com.poulastaa.core.presentation.designsystem.model.ItemClickType
import com.poulastaa.core.presentation.designsystem.noRippleCombineClickable
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.FilterAlbumIcon
import com.poulastaa.core.presentation.designsystem.ui.FilterArtistIcon
import com.poulastaa.core.presentation.designsystem.ui.SadIcon
import com.poulastaa.core.presentation.designsystem.ui.SongIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.main.presentation.components.MainBoxImageCard
import com.poulastaa.main.presentation.components.UiSaveItemType
import com.poulastaa.main.presentation.home.components.HomeExpandedLoadingScreen
import com.poulastaa.main.presentation.home.components.homeCommonContent
import com.poulastaa.main.presentation.main.components.MAIN_TOP_BAR_PADDING

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeExpandedScreen(
    state: HomeUiState,
    scroll: TopAppBarScrollBehavior,
    onAction: (HomeUiAction) -> Unit,
) {
    var cardWidthDp by remember { mutableStateOf(0.dp) }
    val haptic = LocalHapticFeedback.current

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        AnimatedContent(state.canShowUi) { loadingState ->
            when (loadingState) {
                true -> LazyVerticalGrid(
                    columns = GridCells.Fixed(6),
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = ThemChanger.getGradiantBackground()
                            )
                        )
                        .padding(paddingValues)
                        .nestedScroll(scroll.nestedScrollConnection),
                    contentPadding = PaddingValues(
                        top = MAIN_TOP_BAR_PADDING + MaterialTheme.dimens.medium1,
                        start = MaterialTheme.dimens.medium1,
                        end = MaterialTheme.dimens.medium1,
                        bottom = MaterialTheme.dimens.medium1
                    ),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
                ) {
                    state.spotlightItem?.let { item ->
                        item(
                            span = { GridItemSpan(2) }
                        ) {
                            MainBoxImageCard(
                                modifier = Modifier
                                    .noRippleCombineClickable(
                                        onLongClick = {
                                            onAction(
                                                HomeUiAction.OnSavedItemCLick(
                                                    id = item.id,
                                                    type = item.type,
                                                    clickType = ItemClickType.LONG_CLICK
                                                )
                                            )
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        },
                                        onClick = {
                                            onAction(
                                                HomeUiAction.OnSavedItemCLick(
                                                    id = item.id,
                                                    type = item.type,
                                                    clickType = ItemClickType.CLICK
                                                )
                                            )
                                        }
                                    ),
                                title = item.name,
                                urls = item.posters,
                                icon = FilterArtistIcon,
                                returnCardHeight = {
                                    cardWidthDp = it
                                }
                            )
                        }
                    }

                    if (state.savedItems.isNotEmpty()) item(
                        span = { GridItemSpan(maxLineSpan - 2) }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = MaterialTheme.dimens.small1),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(cardWidthDp / 2),
                                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2)
                            ) {
                                state.savedItems.forEach { item ->
                                    MainBoxImageCard(
                                        modifier = Modifier
                                            .noRippleCombineClickable(
                                                onLongClick = {
                                                    onAction(
                                                        HomeUiAction.OnSavedItemCLick(
                                                            id = item.id,
                                                            type = item.type,
                                                            clickType = ItemClickType.LONG_CLICK
                                                        )
                                                    )
                                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                },
                                                onClick = {
                                                    onAction(
                                                        HomeUiAction.OnSavedItemCLick(
                                                            id = item.id,
                                                            type = item.type,
                                                            clickType = ItemClickType.CLICK
                                                        )
                                                    )
                                                }
                                            ),
                                        title = item.name,
                                        urls = item.posters,
                                        icon = when (item.type) {
                                            UiSaveItemType.PLAYLIST -> SongIcon
                                            UiSaveItemType.ALBUM -> FilterAlbumIcon
                                            UiSaveItemType.ARTIST -> FilterArtistIcon
                                            null -> SadIcon
                                        },
                                        shape = if (item.type == UiSaveItemType.ARTIST) CircleShape
                                        else MaterialTheme.shapes.small
                                    )
                                }
                            }

                            Text(
                                text = stringResource(R.string.explore),
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(Modifier.height(MaterialTheme.dimens.small1))

                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .height(cardWidthDp / 2),
                                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2)
                            ) {
                                if (state.staticData.popularSongMix.isNotEmpty()) MainBoxImageCard(
                                    modifier = Modifier.noRippleCombineClickable(
                                        onLongClick = {
                                            onAction(
                                                HomeUiAction.OnExploreTypeItemClick(
                                                    UiHomeExploreType.POPULAR_SONG_MIX,
                                                    ItemClickType.LONG_CLICK
                                                )
                                            )
                                        },
                                        onClick = {
                                            onAction(
                                                HomeUiAction.OnExploreTypeItemClick(
                                                    UiHomeExploreType.POPULAR_SONG_MIX,
                                                    ItemClickType.CLICK
                                                )
                                            )
                                        }
                                    ),
                                    title = stringResource(R.string.popular_song_mix),
                                    urls = state.staticData.popularSongMix.map { it.poster },
                                    icon = SongIcon,
                                )

                                if (state.staticData.favouriteArtistMix.isNotEmpty()) MainBoxImageCard(
                                    modifier = Modifier.noRippleCombineClickable(
                                        onLongClick = {
                                            onAction(
                                                HomeUiAction.OnExploreTypeItemClick(
                                                    UiHomeExploreType.SAVED_ARTIST_SONG_MIX,
                                                    ItemClickType.LONG_CLICK
                                                )
                                            )
                                        },
                                        onClick = {
                                            onAction(
                                                HomeUiAction.OnExploreTypeItemClick(
                                                    UiHomeExploreType.SAVED_ARTIST_SONG_MIX,
                                                    ItemClickType.CLICK
                                                )
                                            )
                                        }
                                    ),
                                    title = stringResource(R.string.favourite_artist_mix),
                                    urls = state.staticData.favouriteArtistMix.map { it.poster },
                                    icon = SongIcon,
                                )

                                if (state.staticData.popularSongFromYourTime.isNotEmpty()) MainBoxImageCard(
                                    modifier = Modifier.noRippleCombineClickable(
                                        onLongClick = {
                                            onAction(
                                                HomeUiAction.OnExploreTypeItemClick(
                                                    UiHomeExploreType.POPULAR_YEAR_MIX,
                                                    ItemClickType.LONG_CLICK
                                                )
                                            )
                                        },
                                        onClick = {
                                            onAction(
                                                HomeUiAction.OnExploreTypeItemClick(
                                                    UiHomeExploreType.POPULAR_YEAR_MIX,
                                                    ItemClickType.CLICK
                                                )
                                            )
                                        }
                                    ),
                                    title = stringResource(R.string.popular_songs_from_your_time),
                                    urls = state.staticData.popularSongFromYourTime.map { it.poster },
                                    icon = SongIcon
                                )

                                if (state.staticData.dayTypeSong.isNotEmpty()) MainBoxImageCard(
                                    modifier = Modifier.noRippleCombineClickable(
                                        onLongClick = {
                                            onAction(
                                                HomeUiAction.OnExploreTypeItemClick(
                                                    UiHomeExploreType.DAY_TYPE_MIX,
                                                    ItemClickType.LONG_CLICK
                                                )
                                            )
                                        },
                                        onClick = {
                                            onAction(
                                                HomeUiAction.OnExploreTypeItemClick(
                                                    UiHomeExploreType.DAY_TYPE_MIX,
                                                    ItemClickType.CLICK
                                                )
                                            )
                                        }
                                    ),
                                    title = stringResource(R.string.lofi_mix),
                                    urls = state.staticData.dayTypeSong.map { it.poster },
                                    icon = SongIcon
                                )
                            }
                        }
                    }

                    homeCommonContent(state, haptic, true, onAction)
                }

                false -> HomeExpandedLoadingScreen(paddingValues, scroll)
            }
        }
    }
}


@Preview(
    device = "spec:width=1280dp,height=800dp,dpi=480",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    device = "spec:width=1280dp,height=800dp,dpi=480",
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Preview() {
    AppThem {
        HomeExpandedScreen(
            state = HomeUiState(),
            scroll = TopAppBarDefaults.enterAlwaysScrollBehavior(),
            onAction = {}
        )
    }
}
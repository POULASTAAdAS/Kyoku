package com.poulastaa.main.presentation.home.components

import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.model.ItemClickType
import com.poulastaa.core.presentation.designsystem.noRippleCombineClickable
import com.poulastaa.core.presentation.designsystem.ui.SongIcon
import com.poulastaa.main.presentation.components.MainBoxImageCard
import com.poulastaa.main.presentation.home.HomeUiAction
import com.poulastaa.main.presentation.home.HomeUiState
import com.poulastaa.main.presentation.home.UiHomeExploreType

internal fun LazyGridScope.homeCompactMediumCommon(
    state: HomeUiState,
    onAction: (HomeUiAction) -> Unit,
) {
    item(
        span = { GridItemSpan(maxLineSpan) }
    ) {
        HomeHeadline(R.string.explore)
    }

    if (state.staticData.popularSongMix.isNotEmpty()) item {
        MainBoxImageCard(
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
    }

    if (state.staticData.favouriteArtistMix.isNotEmpty()) item {
        MainBoxImageCard(
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
    }

    if (state.staticData.popularSongFromYourTime.isNotEmpty()) item {
        MainBoxImageCard(
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
    }

    if (state.staticData.dayTypeSong.isNotEmpty()) item {
        MainBoxImageCard(
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

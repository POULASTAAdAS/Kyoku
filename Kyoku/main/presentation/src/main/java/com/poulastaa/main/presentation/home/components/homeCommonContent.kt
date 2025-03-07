package com.poulastaa.main.presentation.home.components

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.BOTTOM_BAR_HEIGHT
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.model.ItemClickType
import com.poulastaa.core.presentation.designsystem.noRippleCombineClickable
import com.poulastaa.core.presentation.designsystem.removeParentWidthPadding
import com.poulastaa.core.presentation.designsystem.ui.FilterAlbumIcon
import com.poulastaa.core.presentation.designsystem.ui.FilterArtistIcon
import com.poulastaa.core.presentation.designsystem.ui.ShowMoreIcon
import com.poulastaa.core.presentation.designsystem.ui.SongIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.main.presentation.components.MainArtistCard
import com.poulastaa.main.presentation.components.MainBoxImageCard
import com.poulastaa.main.presentation.components.MainImageCard
import com.poulastaa.main.presentation.components.UiMainViewMoreItemType
import com.poulastaa.main.presentation.home.HomeUiAction
import com.poulastaa.main.presentation.home.HomeUiState

internal fun LazyGridScope.homeCommonContent(
    state: HomeUiState,
    haptic: HapticFeedback,
    isMedium: Boolean,
    onAction: (HomeUiAction) -> Unit,
) {
    item(
        span = { GridItemSpan(maxLineSpan) }
    ) {
        HomeHeadline(R.string.artist_you_may_like)
    }

    item(
        span = { GridItemSpan(maxLineSpan) }
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (isMedium) 180.dp else 140.dp)
                .removeParentWidthPadding(
                    if (isMedium) MaterialTheme.dimens.medium2
                    else MaterialTheme.dimens.medium1
                ),
            horizontalArrangement = Arrangement.spacedBy(
                if (isMedium) MaterialTheme.dimens.small2
                else MaterialTheme.dimens.small3
            ),
            contentPadding = PaddingValues(
                start = MaterialTheme.dimens.medium1,
                end = MaterialTheme.dimens.medium1
            )
        ) {
            items(state.staticData.suggestedArtist) { artist ->
                MainArtistCard(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .noRippleCombineClickable(
                            onLongClick = {
                                onAction(HomeUiAction.OnSuggestedArtistLongClick(artist.id))
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            },
                            onClick = {
                                onAction(
                                    HomeUiAction.OnViewMore(
                                        id = artist.id,
                                        type = UiMainViewMoreItemType.EXPLORE_ARTIST
                                    )
                                )
                            }
                        ),
                    artist = artist,
                )
            }

            item {
                Box(
                    modifier = Modifier
                        .aspectRatio(1f),
                    contentAlignment = Alignment.Center
                ) {
                    HomeMoreButton {
                        onAction(HomeUiAction.OnViewMore(UiMainViewMoreItemType.EXPLORE_ARTIST))
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                }
            }
        }
    }

    item(
        span = { GridItemSpan(maxLineSpan) }
    ) {
        HomeHeadline(R.string.popular_albums)
    }

    item(
        span = { GridItemSpan(maxLineSpan) }
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (isMedium) 200.dp else 170.dp)
                .removeParentWidthPadding(
                    if (isMedium) MaterialTheme.dimens.medium2
                    else MaterialTheme.dimens.medium1
                ),
            horizontalArrangement = Arrangement.spacedBy(
                if (isMedium) MaterialTheme.dimens.small1
                else MaterialTheme.dimens.small3
            ),
            contentPadding = PaddingValues(
                start = MaterialTheme.dimens.medium1,
                end = MaterialTheme.dimens.medium1
            )
        ) {
            items(state.staticData.popularAlbum) { album ->
                MainBoxImageCard(
                    modifier = Modifier.noRippleCombineClickable(
                        onLongClick = {
                            onAction(HomeUiAction.OnSuggestedAlbumLongClick(album.id))
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        onClick = {
                            onAction(
                                HomeUiAction.OnViewMore(
                                    id = album.id,
                                    type = UiMainViewMoreItemType.EXPLORE_ALBUM,
                                )
                            )
                        }
                    ),
                    title = album.name,
                    urls = listOf(album.poster),
                    icon = FilterAlbumIcon,
                )
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxSize(.8f)
                        .aspectRatio(1f),
                    contentAlignment = Alignment.Center
                ) {
                    HomeMoreButton {
                        onAction(HomeUiAction.OnViewMore(UiMainViewMoreItemType.EXPLORE_ALBUM))
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                }
            }
        }
    }

    item(
        span = { GridItemSpan(maxLineSpan) }
    ) {
        HomeHeadline(R.string.best_from_artist)
    }

    if (state.staticData.popularArtistSong.isNotEmpty()) items(
        items = state.staticData.popularArtistSong,
        span = { GridItemSpan(maxLineSpan) }
    ) { (artist, songs) ->
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isMedium) 50.dp else 45.dp),
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 3.dp,
                    pressedElevation = 0.dp
                ),
                onClick = {
                    onAction(
                        HomeUiAction.OnViewMore(
                            type = UiMainViewMoreItemType.SUGGESTED_ARTIST_IN_DETAIL,
                            id = artist.id
                        )
                    )
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(end = MaterialTheme.dimens.medium1),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        modifier = Modifier.aspectRatio(1f),
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        onClick = {
                            onAction(
                                HomeUiAction.OnViewMore(
                                    type = UiMainViewMoreItemType.SUGGESTED_ARTIST_IN_DETAIL,
                                    id = artist.id
                                )
                            )
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        }
                    ) {
                        MainImageCard(
                            errorIcon = FilterArtistIcon,
                            url = artist.cover,
                            contentDescription = stringResource(R.string.artist),
                            modifier = Modifier.fillMaxSize(),
                            iconColor = MaterialTheme.colorScheme.background
                        )
                    }

                    Spacer(Modifier.width(MaterialTheme.dimens.medium1))

                    Column(
                        modifier = Modifier
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.more_from_artist),
                            color = MaterialTheme.colorScheme.primary.copy(.7f),
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize
                        )

                        Text(
                            modifier = Modifier.offset(y = (-8).dp),
                            text = artist.name,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(Modifier.weight(1f))

                    Icon(
                        modifier = Modifier.offset(y = 2.dp),
                        imageVector = ShowMoreIcon,
                        contentDescription = stringResource(R.string.view_more),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(Modifier.height(MaterialTheme.dimens.medium1))

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isMedium) 180.dp else 140.dp)
                    .removeParentWidthPadding(
                        if (isMedium) MaterialTheme.dimens.medium2
                        else MaterialTheme.dimens.medium1
                    ),
                horizontalArrangement = Arrangement.spacedBy(
                    if (isMedium) MaterialTheme.dimens.small1
                    else MaterialTheme.dimens.small3
                ),
                contentPadding = PaddingValues(
                    start = if (isMedium) MaterialTheme.dimens.medium2
                    else MaterialTheme.dimens.medium1,
                    end = if (isMedium) MaterialTheme.dimens.medium2
                    else MaterialTheme.dimens.medium1
                )
            ) {
                items(songs) { song ->
                    MainBoxImageCard(
                        modifier = Modifier.noRippleCombineClickable(
                            onLongClick = {
                                onAction(
                                    HomeUiAction.OnSuggestArtistSongClick(
                                        songId = song.id,
                                        artistId = artist.id,
                                        clickType = ItemClickType.LONG_CLICK
                                    )
                                )
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            },
                            onClick = {
                                onAction(
                                    HomeUiAction.OnSuggestArtistSongClick(
                                        songId = song.id,
                                        artistId = artist.id,
                                        clickType = ItemClickType.CLICK
                                    )
                                )
                            }
                        ),
                        title = song.title,
                        urls = listOf(song.poster),
                        icon = SongIcon,
                    )
                }

                item {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(.8f)
                            .aspectRatio(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        HomeMoreButton {
                            onAction(
                                HomeUiAction.OnViewMore(
                                    type = UiMainViewMoreItemType.SUGGESTED_ARTIST_IN_DETAIL,
                                    id = artist.id
                                )
                            )
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        }
                    }
                }
            }
        }
    }

    item {
        Spacer(Modifier.height(if (isMedium) BOTTOM_BAR_HEIGHT + MaterialTheme.dimens.medium3 else BOTTOM_BAR_HEIGHT))
    }
}

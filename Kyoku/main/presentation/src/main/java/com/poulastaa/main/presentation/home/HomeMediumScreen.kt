package com.poulastaa.main.presentation.home

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.model.ItemClickType
import com.poulastaa.core.presentation.designsystem.noRippleCombineClickable
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.FilterArtistIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.main.presentation.components.MainBoxImageCard
import com.poulastaa.main.presentation.home.components.HomeMediumLoadingScreen
import com.poulastaa.main.presentation.home.components.HomeSavedItemCard
import com.poulastaa.main.presentation.home.components.homeCommonContent
import com.poulastaa.main.presentation.home.components.homeCompactMediumCommon
import com.poulastaa.main.presentation.main.components.MAIN_TOP_BAR_PADDING

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeMediumScreen(
    state: HomeUiState,
    topBarScroll: TopAppBarScrollBehavior,
    onAction: (HomeUiAction) -> Unit,
) {
    val itemPaddingHeight = MaterialTheme.dimens.small3 * 2
    var cardWidthDp by remember { mutableStateOf(0.dp) }

    val haptic = LocalHapticFeedback.current

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        AnimatedContent(state.canShowUi) { animatedState ->
            when (animatedState) {
                true -> LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = ThemModeChanger.getGradiantBackground()
                            )
                        )
                        .padding(paddingValues)
                        .nestedScroll(topBarScroll.nestedScrollConnection),
                    contentPadding = PaddingValues(
                        top = MAIN_TOP_BAR_PADDING + MaterialTheme.dimens.medium1,
                        start = MaterialTheme.dimens.medium2,
                        end = MaterialTheme.dimens.medium2,
                        bottom = MaterialTheme.dimens.medium2
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
                                    cardWidthDp = it - itemPaddingHeight
                                }
                            )
                        }
                    }

                    if (state.savedItems.size >= 2) item(
                        span = { GridItemSpan(2) }
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
                        ) {
                            state.savedItems.getOrNull(0)?.let { item ->
                                HomeSavedItemCard(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(cardWidthDp / 3)
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
                                    item = item,
                                )
                            }

                            state.savedItems.getOrNull(1)?.let { item ->
                                HomeSavedItemCard(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(cardWidthDp / 3)
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
                                    item = item,
                                )
                            }

                            state.savedItems.getOrNull(2)?.let { item ->
                                HomeSavedItemCard(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(cardWidthDp / 3)
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
                                    item = item,
                                )
                            }
                        }
                    }

                    if (state.savedItems.size >= 6) items(state.savedItems) { item ->
                        HomeSavedItemCard(
                            modifier = Modifier
                                .height(75.dp)
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
                            item = item
                        )
                    }

                    homeCompactMediumCommon(haptic, state, onAction)
                    homeCommonContent(state, haptic, true, onAction)
                }

                false -> HomeMediumLoadingScreen(paddingValues, topBarScroll)
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    device = "spec:width=800dp,height=1280dp,dpi=480",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    device = "spec:width=800dp,height=1280dp,dpi=480",
)
@Composable
private fun Preview() {
    AppThem {
        Surface {
            HomeMediumScreen(
                topBarScroll = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                state = prevData,
                onAction = {}
            )
        }
    }
}
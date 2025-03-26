package com.poulastaa.main.presentation.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.BOTTOM_BAR_HEIGHT
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.model.ItemClickType
import com.poulastaa.core.presentation.designsystem.noRippleCombineClickable
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.main.presentation.home.components.HomeCompactLoadingScreen
import com.poulastaa.main.presentation.home.components.HomeSavedItemCard
import com.poulastaa.main.presentation.home.components.homeCommonContent
import com.poulastaa.main.presentation.home.components.homeCompactMediumCommon
import com.poulastaa.main.presentation.main.components.MAIN_TOP_BAR_PADDING

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeCompactScreen(
    state: HomeUiState,
    topBarScroll: TopAppBarScrollBehavior,
    onAction: (HomeUiAction) -> Unit,
) {
    val haptic = LocalHapticFeedback.current

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        AnimatedContent(state.canShowUi) { animationState ->
            when (animationState) {
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
                        start = MaterialTheme.dimens.medium1,
                        end = MaterialTheme.dimens.medium1,
                        bottom = BOTTOM_BAR_HEIGHT
                    ),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
                ) {
                    if (state.savedItems.isNotEmpty()) items(
                        items = state.savedItems + if (state.spotlightItem != null) listOf(state.spotlightItem) else emptyList(),
                        span = { GridItemSpan(2) }
                    ) { item ->
                        HomeSavedItemCard(
                            modifier = Modifier
                                .height(60.dp)
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
                    homeCommonContent(state, haptic, false, onAction)
                }

                false -> HomeCompactLoadingScreen(paddingValues, topBarScroll)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            HomeCompactScreen(
                state = prevData,
                topBarScroll = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                onAction = {}
            )
        }
    }
}
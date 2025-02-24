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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.ThemChanger
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.FilterArtistIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.main.presentation.components.MainBoxImageCard
import com.poulastaa.main.presentation.home.components.HomeMediumLoadingScreen
import com.poulastaa.main.presentation.home.components.HomeSavedItemCard
import com.poulastaa.main.presentation.main.components.MAIN_TOP_BAR_PADDING

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeMediumScreen(
    state: HomeUiState,
    scroll: TopAppBarScrollBehavior,
    onAction: (HomeUiAction) -> Unit,
) {
    val density = LocalDensity.current
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
                                colors = ThemChanger.getGradiantBackground()
                            )
                        )
                        .padding(paddingValues)
                        .nestedScroll(scroll.nestedScrollConnection),
                    contentPadding = PaddingValues(
                        top = MAIN_TOP_BAR_PADDING,
                        start = MaterialTheme.dimens.medium2,
                        end = MaterialTheme.dimens.medium2,
                        bottom = MaterialTheme.dimens.medium2
                    ),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
                ) {
                    if (state.savedItems.isNotEmpty()) item(
                        span = { GridItemSpan(2) }
                    ) {
                        MainBoxImageCard(
                            modifier = Modifier
                                .onSizeChanged { size ->
                                    cardWidthDp = with(density) { (size.width - 20).toDp() }
                                },
                            title = state.savedItems.first().name,
                            urls = state.savedItems.first().posters,
                            icon = FilterArtistIcon,
                        )
                    }

                    if (state.savedItems.size >= 2) item(
                        span = { GridItemSpan(2) }
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
                        ) {
                            HomeSavedItemCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(cardWidthDp / 3),
                                item = state.savedItems[1],
                            )

                            state.savedItems.getOrNull(2)?.let {
                                HomeSavedItemCard(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(cardWidthDp / 3),
                                    item = it,
                                )
                            }

                            state.savedItems.getOrNull(3)?.let {
                                HomeSavedItemCard(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(cardWidthDp / 3),
                                    item = it,
                                )
                            }
                        }
                    }

                    if (state.savedItems.size >= 6) items(state.savedItems) { item ->
                        HomeSavedItemCard(
                            modifier = Modifier.height(70.dp),
                            item = item
                        )
                    }

                    homeCommonContent(state, haptic, true, onAction)
                }

                false -> HomeMediumLoadingScreen(paddingValues, scroll)
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
                scroll = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                state = prevData,
                onAction = {}
            )
        }
    }
}
package com.poulastaa.main.presentation.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalHapticFeedback
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.designsystem.ui.gradiantBackground
import com.poulastaa.main.presentation.home.components.HomeExpandedLoadingScreen
import com.poulastaa.main.presentation.home.components.MAIN_TOP_BAR_PADDING

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeExpandedScreen(
    state: HomeUiState,
    scroll: TopAppBarScrollBehavior,
    onAction: (HomeUiAction) -> Unit,
) {
    val haptic = LocalHapticFeedback.current

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        AnimatedContent(state.canShowUi) {
            when (it) {
                true -> LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = gradiantBackground()
                            )
                        )
                        .padding(paddingValues)
                        .nestedScroll(scroll.nestedScrollConnection),
                    contentPadding = PaddingValues(
                        top = MAIN_TOP_BAR_PADDING,
                        start = MaterialTheme.dimens.medium1,
                        end = MaterialTheme.dimens.medium1,
                        bottom = MaterialTheme.dimens.medium1
                    )
                ) {

                }

                false -> HomeExpandedLoadingScreen(paddingValues, scroll)
            }
        }
    }
}
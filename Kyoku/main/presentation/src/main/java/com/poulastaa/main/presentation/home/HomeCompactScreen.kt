package com.poulastaa.main.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalHapticFeedback
import com.poulastaa.core.presentation.designsystem.model.UiUser
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.designsystem.ui.gradiantBackground
import com.poulastaa.main.presentation.home.components.HomeTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeCompactScreen(
    user: UiUser,
    toggleDrawer: () -> Unit,
) {
    val haptic = LocalHapticFeedback.current
    val scroll = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
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
                    start = MaterialTheme.dimens.medium1,
                    end = MaterialTheme.dimens.medium1,
                    bottom = MaterialTheme.dimens.medium1
                )
            ) {

            }

            HomeTopBar(
                scroll = scroll,
                user = user,
                dayStatus = "Good Morning",
                onSearchClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                },
                onProfileClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    toggleDrawer()
                }
            )
        }
    }
}
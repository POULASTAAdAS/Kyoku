package com.poulastaa.main.presentation.home

import android.app.Activity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poulastaa.core.presentation.designsystem.KyokuWindowSize

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun HomeRootScreen(
    viewmodel: HomeViewmodel,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    val activity = LocalContext.current as Activity
    val windowSizeClass = calculateWindowSizeClass(activity)

    val state by viewmodel.state.collectAsStateWithLifecycle()

    KyokuWindowSize(
        windowSizeClass = windowSizeClass,
        compactContent = {
            HomeCompactScreen(
                scrollBehavior = scrollBehavior,
                state = state,
            )
        },
        mediumContent = {
            HomeCompactScreen(
                scrollBehavior = scrollBehavior,
                state = state,
            )
        },
        expandedContent = {
            HomeCompactScreen(
                scrollBehavior = scrollBehavior,
                state = state,
            )
        }
    )
}
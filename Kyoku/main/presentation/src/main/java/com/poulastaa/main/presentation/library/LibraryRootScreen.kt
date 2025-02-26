package com.poulastaa.main.presentation.library

import android.app.Activity
import androidx.compose.material3.BottomAppBarScrollBehavior
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poulastaa.core.presentation.designsystem.KyokuWindowSize

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun LibraryRootScreen(
    topBarScroll: TopAppBarScrollBehavior,
    viewmodel: LibraryViewmodel = hiltViewModel(),
) {
    val activity = LocalContext.current as Activity
    val windowSizeClass = calculateWindowSizeClass(activity)

    val state by viewmodel.state.collectAsStateWithLifecycle()

    KyokuWindowSize(
        windowSizeClass = windowSizeClass,
        compactContent = {
            LibraryCompactScreen(
                state = state,
                topAppBarScroll = topBarScroll,
                onAction = viewmodel::onAction
            )
        },
        mediumContent = {
            LibraryMediumScreen(
                state = state,
                topAppBarScroll = topBarScroll,
                onAction = viewmodel::onAction
            )
        },
        expandedContent = {
            LibraryExpandedScreen(
                state = state,
                topAppBarScroll = topBarScroll,
                onAction = viewmodel::onAction
            )
        }
    )
}
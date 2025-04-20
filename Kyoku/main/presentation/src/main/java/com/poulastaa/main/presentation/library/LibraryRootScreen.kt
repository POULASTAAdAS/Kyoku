package com.poulastaa.main.presentation.library

import android.app.Activity
import android.widget.Toast
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
import com.poulastaa.core.presentation.designsystem.ObserveAsEvent
import com.poulastaa.main.domain.model.MainAllowedNavigationScreens
import com.poulastaa.main.presentation.home.mapper.toMainAllowedNavigationScreensLibrary
import com.poulastaa.main.presentation.home.mapper.toNavigateToEditSavedItemScreen

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun LibraryRootScreen(
    topBarScroll: TopAppBarScrollBehavior,
    viewmodel: LibraryViewmodel = hiltViewModel(),
    navigate: (MainAllowedNavigationScreens) -> Unit,
) {
    val activity = LocalContext.current as Activity
    val windowSizeClass = calculateWindowSizeClass(activity)

    val state by viewmodel.state.collectAsStateWithLifecycle()

    ObserveAsEvent(viewmodel.uiEvent) { event ->
        when (event) {
            is LibraryUiEvent.EmitToast -> Toast.makeText(
                activity,
                event.message.asString(activity),
                Toast.LENGTH_LONG
            ).show()

            is LibraryUiEvent.NavigateToView -> navigate(event.toMainAllowedNavigationScreensLibrary())
            is LibraryUiEvent.NavigateToSavedScreen -> navigate(event.toNavigateToEditSavedItemScreen())
        }
    }

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
package com.poulastaa.settings.presentation

import android.app.Activity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poulastaa.core.presentation.designsystem.KyokuWindowSize
import com.poulastaa.core.presentation.designsystem.ObserveAsEvent

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun SettingsRootScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onLogOut: () -> Unit,
) {
    val context = LocalContext.current as Activity
    val windowSize = calculateWindowSizeClass(context)
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvent(viewModel.uiEvent) { event ->
        when (event) {
            is SettingsUiEvent.OnLogOutSuccess -> onLogOut()
        }
    }

    // todo add medium and expanded content

    KyokuWindowSize(
        windowSizeClass = windowSize,
        compactContent = {
            SettingsCompactScreen(
                state = state,
                onAction = viewModel::onAction
            )
        },
        mediumContent = {
            SettingsCompactScreen(
                state = state,
                onAction = viewModel::onAction
            )
        },
        expandedContent = {
            SettingsCompactScreen(
                state = state,
                onAction = viewModel::onAction
            )
        }
    )
}
package com.poulastaa.settings.presentation

import android.app.Activity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poulastaa.core.presentation.designsystem.KyokuWindowSize
import com.poulastaa.core.presentation.designsystem.ObserveAsEvent
import com.poulastaa.core.presentation.designsystem.ThemChanger

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun SettingsRootScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onLogOut: () -> Unit,
) {
    val context = LocalContext.current as Activity
    val windowSize = calculateWindowSizeClass(context)
    val state by viewModel.state.collectAsStateWithLifecycle()

    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val triple = ThemChanger.compute(
        density,
        configuration,
        state.offset,
        state.themChangeAnimationTime,
        resetAnimation = { viewModel.onAction(SettingsUiAction.ResetRevelAnimation) }
    )

    ObserveAsEvent(viewModel.uiEvent) { event ->
        when (event) {
            is SettingsUiEvent.OnLogOutSuccess -> onLogOut()
        }
    }

    Box(Modifier.fillMaxSize()) {
        KyokuWindowSize(
            windowSizeClass = windowSize,
            compactContent = {
                SettingsCompactScreen(
                    state = state,
                    onAction = viewModel::onAction,
                    navigateBack = {

                    }
                )
            },
            mediumContent = {

            },
            expandedContent = {

            }
        )

        Canvas(modifier = Modifier.fillMaxSize()) {
            if (triple.revealSize.value > 0) {
                drawCircle(
                    color = triple.backgroundColor,
                    radius = triple.revealSize.value,
                    center = triple.center
                )
            }
        }
    }
}
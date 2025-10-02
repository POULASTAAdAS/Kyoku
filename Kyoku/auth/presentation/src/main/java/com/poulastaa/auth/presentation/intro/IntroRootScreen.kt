package com.poulastaa.auth.presentation.intro

import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poulastaa.auth.presentation.intro.model.IntroAllowedNavigationScreens
import com.poulastaa.core.domain.SavedScreen
import com.poulastaa.core.presentation.KyokuWindowSize
import com.poulastaa.core.presentation.designsystem.ObserveAsEvent
import com.poulastaa.core.presentation.ui.dimens

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal fun IntroRootScreen(
    viewModel: IntroViewmodel,
    navigateRoot: (screen: SavedScreen) -> Unit,
    navigateLocal: (screen: IntroAllowedNavigationScreens) -> Unit,
) {
    val context = LocalContext.current
    val activity = LocalActivity.current ?: return
    val windowSizeClass = calculateWindowSizeClass(activity)

    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvent(viewModel.uiEvent) { event ->
        when (event) {
            is IntroUiEvent.EmitToast -> Toast.makeText(
                context,
                event.message.asString(context),
                Toast.LENGTH_LONG
            ).show()

            is IntroUiEvent.GoogleAuthSuccess -> navigateRoot(event.screen)
            is IntroUiEvent.Navigate -> navigateLocal(event.screen)
        }
    }

    KyokuWindowSize(
        windowSizeClass = windowSizeClass,
        compactContent = {
            IntroVerticalScreen(
                state = state,
                onAction = viewModel::onAction
            )
        },
        mediumContent = {
            IntroVerticalScreen(
                modifier = Modifier.padding(MaterialTheme.dimens.small3),
                state = state,
                onAction = viewModel::onAction
            )
        },
        expandedSmallContent = {
            IntroHorizontalCompactScreen(
                state = state,
                onAction = viewModel::onAction
            )
        },
        expandedCompactContent = {
            IntroHorizontalCompactScreen(
                state = state,
                onAction = viewModel::onAction
            )
        },
        expandedLargeContent = {
            IntroHorizontalLargeScreen(
                state = state,
                onAction = viewModel::onAction
            )
        }
    )
}
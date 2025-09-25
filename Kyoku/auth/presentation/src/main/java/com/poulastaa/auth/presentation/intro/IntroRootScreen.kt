package com.poulastaa.auth.presentation.intro

import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poulastaa.auth.presentation.intro.model.IntroNavigationScreens
import com.poulastaa.core.domain.SavedScreen
import com.poulastaa.core.presentation.KyokuWindowSize
import com.poulastaa.core.presentation.designsystem.ObserveAsEvent

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal fun IntroRootScreen(
    viewModel: IntroViewmodel,
    navigateRoot: (screen: SavedScreen) -> Unit,
    navigateLocal: (screen: IntroNavigationScreens) -> Unit,
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
            IntroHorizontalScreen(
                state = state,
                onAction = viewModel::onAction
            )
        },
        expandedCompactContent = {

        },
        mediumContent = {

        },
        expandedContent = {

        }
    )
}
package com.poulastaa.view.presentation.saved

import android.app.Activity
import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poulastaa.core.presentation.designsystem.KyokuWindowSize
import com.poulastaa.core.presentation.designsystem.ObserveAsEvent
import com.poulastaa.view.domain.model.ViewSavedAllowedNavigationScreen

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ViewSavedItemRootScreen(
    type: ViewSavedUiItemType,
    navigate: (ViewSavedAllowedNavigationScreen) -> Unit,
    navigateBack: () -> Unit,
) {
    val viewmodel = hiltViewModel<ViewSavedItemViewmodel>()

    LaunchedEffect(type) {
        viewmodel.init(type)
    }

    val context = LocalContext.current as Activity
    val windowSizeClass = calculateWindowSizeClass(context)
    val state by viewmodel.state.collectAsStateWithLifecycle()

    ObserveAsEvent(viewmodel.uiEvent) { event ->
        when (event) {
            is ViewSavedUiEvent.EmitToast -> Toast.makeText(
                context,
                event.message.asString(context),
                Toast.LENGTH_LONG
            ).show()

            is ViewSavedUiEvent.Navigate -> navigate(event.screen)
        }
    }

    KyokuWindowSize(
        windowSizeClass = windowSizeClass,
        compactContent = {
            ViewSavedItemCompactScreen(
                state = state,
                onAction = viewmodel::onAction,
                navigateBack = navigateBack
            )
        },
        mediumContent = {
            ViewSavedItemCompactScreen(
                state = state,
                onAction = viewmodel::onAction,
                navigateBack = navigateBack
            )
        },
        expandedContent = {
            ViewSavedItemExtendedScreen(
                state = state,
                onAction = viewmodel::onAction,
                navigateBack = navigateBack
            )
        }
    )
}
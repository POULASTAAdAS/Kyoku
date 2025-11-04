package com.poulastaa.board.presentation.set_bdate

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poulastaa.core.presentation.StateFullKyokuWindowSize
import com.poulastaa.core.presentation.ui.dimens

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun SetBDateRootScreen(
    navigateBack: () -> Unit,
) {
    val activity = LocalActivity.current ?: return
    val viewmodel = hiltViewModel<SetUpBDateViewmodel>()
    val state by viewmodel.state.collectAsStateWithLifecycle()

    StateFullKyokuWindowSize(
        sharedFlow = viewmodel.eventManager.rootEvent,
        windowSizeClass = calculateWindowSizeClass(activity),
        compactContent = {
            SetBDateVerticalScreen(
                state = state,
                navigateBack = navigateBack,
                onAction = viewmodel::onAction
            )
        },
        mediumContent = {
            SetBDateVerticalScreen(
                width = 0.7f,
                modifier = Modifier.padding(MaterialTheme.dimens.small3),
                state = state,
                navigateBack = navigateBack,
                onAction = viewmodel::onAction
            )
        },
        expandedSmallContent = {
            SetBDateVerticalScreen(
                state = state,
                navigateBack = navigateBack,
                onAction = viewmodel::onAction
            )
        },
        expandedCompactContent = {
            SetBDateVerticalScreen(
                width = 0.6f,
                modifier = Modifier.padding(MaterialTheme.dimens.small3),
                state = state,
                navigateBack = navigateBack,
                onAction = viewmodel::onAction
            )
        },
        expandedLargeContent = {
            SetBDateVerticalScreen(
                width = 0.55f,
                modifier = Modifier.padding(MaterialTheme.dimens.small3),
                state = state,
                navigateBack = navigateBack,
                onAction = viewmodel::onAction
            )
        }
    )
}
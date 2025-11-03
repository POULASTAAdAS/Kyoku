package com.poulastaa.board.presentation.set_bdate

import androidx.activity.compose.LocalActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poulastaa.core.presentation.SnackBarUiEvent
import com.poulastaa.core.presentation.StateFullKyokuWindowSize
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.emptyFlow

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun SetBDateRootScreen(
    navigateToSelectGenre: () -> Unit,
    navigateBack: () -> Unit,
) {
    val activity = LocalActivity.current ?: return
    val viewmodel = hiltViewModel<SetUpBDateViewmodel>()
    val state by viewmodel.state.collectAsStateWithLifecycle()

    StateFullKyokuWindowSize(
        sharedFlow = emptyFlow<SnackBarUiEvent>() as SharedFlow<SnackBarUiEvent>,
        windowSizeClass = calculateWindowSizeClass(activity),
        compactContent = {

        },
        mediumContent = {

        },
        expandedSmallContent = {

        },
        expandedCompactContent = {

        },
        expandedLargeContent = {

        }
    )
}
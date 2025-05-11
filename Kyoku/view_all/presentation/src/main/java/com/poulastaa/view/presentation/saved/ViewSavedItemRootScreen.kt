package com.poulastaa.view.presentation.saved

import android.app.Activity
import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poulastaa.core.presentation.designsystem.KyokuWindowSize
import com.poulastaa.core.presentation.designsystem.ObserveAsEvent
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.ui.components.AppBasicDialog
import com.poulastaa.core.presentation.ui.components.crate_playlist.CratePlaylistBottomBar
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

    val bottomSheet = rememberModalBottomSheetState(confirmValueChange = { false })

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

    LaunchedEffect(state.isNewPlaylistDialogOpen) {
        if (state.isNewPlaylistDialogOpen) bottomSheet.show()
        else bottomSheet.hide()
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

    if (state.isSelectedCancelDialogOpen) AppBasicDialog(
        title = stringResource(R.string.clear_selected_items),
        onConformClick = {
            viewmodel.onAction(ViewSavedUiAction.OnClearAllSelectedClick)
        },
        onDismissRequest = {
            viewmodel.onAction(ViewSavedUiAction.OnClearSelectedDialogToggle)
        }
    )

    if (state.isDeleteDialogOpen) AppBasicDialog(
        flipColor = true,
        title = stringResource(R.string.delete_all_selected_items),
        onConformClick = {
            viewmodel.onAction(ViewSavedUiAction.OnDeleteAllConformClick)
        },
        onDismissRequest = {
            viewmodel.onAction(ViewSavedUiAction.OnDeleteAllToggleClick)
        }
    )

    if (bottomSheet.isVisible) CratePlaylistBottomBar(
        created = { playlistId ->
            viewmodel.onAction(ViewSavedUiAction.OnNewPlaylistCreated(playlistId))
        },
        canceled = {
            viewmodel.onAction(ViewSavedUiAction.OnAddNewItemClick)
        }
    )
}
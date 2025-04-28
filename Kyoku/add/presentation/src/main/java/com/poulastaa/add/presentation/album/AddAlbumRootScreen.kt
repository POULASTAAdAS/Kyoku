package com.poulastaa.add.presentation.album

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.poulastaa.core.presentation.designsystem.KyokuWindowSize
import com.poulastaa.core.presentation.designsystem.ObserveAsEvent
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.ui.components.AppBasicDialog

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddAlbumRootScreen(
    navigateBack: () -> Unit,
) {
    val viewmodel = hiltViewModel<AddAlbumViewmodel>()
    val state by viewmodel.state.collectAsStateWithLifecycle()
    val album = viewmodel.album.collectAsLazyPagingItems()

    val context = LocalContext.current as Activity
    val windowSizeClass = calculateWindowSizeClass(context)

    val scroll = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val config = LocalConfiguration.current

    ObserveAsEvent(viewmodel.uiEvent) {
        when (it) {
            is AddAlbumUiEvent.EmitToast -> Toast.makeText(
                context,
                it.message.asString(context),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    KyokuWindowSize(
        windowSizeClass = windowSizeClass,
        compactContent = {
            AddAlbumCompactScreen(
                scroll = scroll,
                state = state,
                album = album,
                onAction = viewmodel::onAction,
                navigateBack = navigateBack
            )
        },
        mediumContent = {
            AddAlbumCompactScreen(
                scroll = scroll,
                state = state,
                album = album,
                onAction = viewmodel::onAction,
                navigateBack = navigateBack
            )
        },
        expandedContent = {
            AddAlbumExtendedScreen(
                isExtendedSearch = config.screenWidthDp > 980,
                scroll = scroll,
                state = state,
                album = album,
                onAction = viewmodel::onAction,
                navigateBack = navigateBack
            )
        }
    )

    if (state.isClearAllDialogOpen) AppBasicDialog(
        title = stringResource(R.string.cancel_saving_album),
        onConformClick = {
            viewmodel.onAction(AddAlbumUiAction.OnSaveCancelClick)
        },
        onDismissRequest = {
            viewmodel.onAction(AddAlbumUiAction.OnClearAllDialogToggle)
        }
    )

    BackHandler(
        enabled = state.isSavingAlbums || state.isEditEnabled
    ) {
        if (state.isSavingAlbums) Toast.makeText(
            context,
            context.getString(R.string.saving_album_wait),
            Toast.LENGTH_LONG
        ).show()
        else viewmodel.onAction(AddAlbumUiAction.OnClearAllDialogToggle)
    }
}
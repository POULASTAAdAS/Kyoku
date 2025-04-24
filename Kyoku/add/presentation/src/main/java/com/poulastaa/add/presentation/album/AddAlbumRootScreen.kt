package com.poulastaa.add.presentation.album

import android.app.Activity
import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.poulastaa.core.presentation.designsystem.KyokuWindowSize
import com.poulastaa.core.presentation.designsystem.ObserveAsEvent

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

        }
    )
}
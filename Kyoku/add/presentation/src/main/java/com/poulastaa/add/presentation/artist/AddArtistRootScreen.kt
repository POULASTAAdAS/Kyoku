package com.poulastaa.add.presentation.artist

import android.app.Activity
import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.poulastaa.core.presentation.designsystem.KyokuWindowSize
import com.poulastaa.core.presentation.designsystem.ObserveAsEvent

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddArtistRootScreen(
    navigateBack: () -> Unit,
) {
    val viewmodel = hiltViewModel<AddArtistViewmodel>()
    val state by viewmodel.state.collectAsStateWithLifecycle()
    val artist = viewmodel.artist.collectAsLazyPagingItems()

    val context = LocalContext.current as Activity
    val windowSizeClass = calculateWindowSizeClass(context)

    val scroll = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val config = LocalConfiguration.current

    ObserveAsEvent(viewmodel.uiEvent) { event ->
        when (event) {
            is AddArtistUiEvent.EmitToast -> Toast.makeText(
                context,
                event.message.asString(context),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    KyokuWindowSize(
        windowSizeClass = windowSizeClass,
        compactContent = {
            AddArtistCompactScreen(
                scroll = scroll,
                columns = 4,
                state = state,
                artist = artist,
                onAction = viewmodel::onAction,
                navigateBack = navigateBack
            )
        },
        mediumContent = {
            AddArtistCompactScreen(
                scroll = scroll,
                columns = 5,
                state = state,
                artist = artist,
                onAction = viewmodel::onAction,
                navigateBack = navigateBack
            )
        },
        expandedContent = {
            AddArtistExtendedScreen(
                scroll = scroll,
                columns = 6,
                isExtendedSearch = config.screenWidthDp > 980,
                state = state,
                artist = artist,
                onAction = viewmodel::onAction,
                navigateBack = navigateBack
            )
        }
    )
}
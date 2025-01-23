package com.poulastaa.setup.presentation.pic_genre

import android.app.Activity
import android.widget.Toast
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.poulastaa.core.presentation.ui.KyokuWindowSize
import com.poulastaa.core.presentation.ui.ObserveAsEvent

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun PicGenreRootScreen(
    viewmodel: PicGenreViewmodel = hiltViewModel(),
    navigateToPicArtist: () -> Unit,
) {
    val activity = LocalContext.current as Activity
    val state by viewmodel.state.collectAsStateWithLifecycle()
    val genre = viewmodel.genre.collectAsLazyPagingItems()
    val config = LocalConfiguration.current

    val windowSizeClass = calculateWindowSizeClass(activity)

    ObserveAsEvent(viewmodel.event) { event ->
        when (event) {
            is PicGenreUiEvent.EmitToast -> Toast.makeText(
                activity,
                event.message.asString(activity),
                Toast.LENGTH_LONG
            ).show()

            PicGenreUiEvent.OnSuccess -> navigateToPicArtist()
        }
    }

    KyokuWindowSize(
        windowSizeClass = windowSizeClass,
        compactContent = {
            PicGenreCompactScreen(
                state = state,
                gridSize = 2,
                cardHeight = 80.dp,
                genre = genre,
                onAction = viewmodel::onAction
            )
        },
        mediumContent = {
            PicGenreCompactScreen(
                state = state,
                gridSize = 3,
                cardHeight = 95.dp,
                genre = genre,
                onAction = viewmodel::onAction
            )
        },
        expandedContent = {
            if (config.screenWidthDp > 980) PicGenreExtendedScreen(
                state = state,
                gridSize = 3,
                cardHeight = 95.dp,
                genre = genre,
                onAction = viewmodel::onAction
            ) else PicGenreExtendedScreen(
                state = state,
                gridSize = 2,
                cardHeight = 80.dp,
                genre = genre,
                onAction = viewmodel::onAction
            )
        }
    )
}
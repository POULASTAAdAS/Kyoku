package com.poulastaa.view.presentation.others

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poulastaa.core.domain.model.ViewType
import com.poulastaa.core.presentation.designsystem.KyokuWindowSize
import com.poulastaa.core.presentation.designsystem.ObserveAsEvent
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.view.domain.model.ViewOtherAllowedNavigationScreen
import com.poulastaa.view.presentation.components.ViewPlaylistFavouriteEmptyScreen
import com.poulastaa.view.presentation.model.UiViewPrevSong
import com.poulastaa.view.presentation.model.UiViewType

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ViewOtherRootScreen(
    otherId: Long,
    viewType: ViewType,
    navigate: (ViewOtherAllowedNavigationScreen) -> Unit,
    navigateBack: () -> Unit,
) {
    val viewmodel = hiltViewModel<ViewOtherViewmodel>()
    LaunchedEffect(otherId) {
        viewmodel.init(otherId, viewType)
    }

    val context = LocalContext.current as Activity
    val windowSizeClass = calculateWindowSizeClass(context)
    val state by viewmodel.state.collectAsStateWithLifecycle()
    val scroll = TopAppBarDefaults.enterAlwaysScrollBehavior()

    ObserveAsEvent(viewmodel.uiState) { event ->
        when (event) {
            is ViewOtherUiEvent.EmitToast -> Toast.makeText(
                context,
                event.message.asString(context),
                Toast.LENGTH_LONG
            ).show()

            is ViewOtherUiEvent.Navigate -> navigate(event.screens)
        }
    }

    KyokuWindowSize(
        windowSizeClass = windowSizeClass,
        compactContent = {
            if (state.listOfSongs.isEmpty() && state.loadingType is LoadingType.Content) ViewPlaylistFavouriteEmptyScreen(
                modifier = Modifier.fillMaxSize(),
                title = state.root.title,
                isPlaylist = state.type == UiViewType.PLAYLIST,
                onCheckOutClick = {

                },
                navigateBack = navigateBack
            )
            else ViewOtherCompactScreen(
                scroll = scroll,
                state = state,
                onAction = viewmodel::onAction,
                navigateBack = navigateBack
            )
        },
        mediumContent = {
            if (state.listOfSongs.isEmpty() && state.loadingType is LoadingType.Content) ViewPlaylistFavouriteEmptyScreen(
                modifier = Modifier.fillMaxSize(),
                title = state.root.title,
                isPlaylist = state.type == UiViewType.PLAYLIST,
                onCheckOutClick = {

                },
                navigateBack = navigateBack
            )
            else ViewOtherMediumScreen(
                scroll = scroll,
                state = state,
                onAction = viewmodel::onAction,
                navigateBack = navigateBack
            )
        },
        expandedContent = {
            if (state.listOfSongs.isEmpty() && state.loadingType is LoadingType.Content) ViewPlaylistFavouriteEmptyScreen(
                modifier = Modifier.fillMaxSize(),
                title = state.root.title,
                isPlaylist = state.type == UiViewType.PLAYLIST,
                verticalArrangement = Arrangement.Center,
                onCheckOutClick = {

                },
                navigateBack = navigateBack
            )
            else ViewOtherExpandedScreen(
                scroll = scroll,
                state = state,
                onAction = viewmodel::onAction,
                navigateBack = navigateBack
            )
        }
    )

}

internal val PREV_VIEW_DATA = ViewOtherUiState(
    loadingType = LoadingType.Content,
    type = UiViewType.PLAYLIST,
    root = UiRoot(
        title = "That Cool Playlist",
    ),
    listOfSongs = (1..10).map {
        UiViewPrevSong(
            id = it.toLong(),
            title = "That Cool Song $it",
            artists = "Those Cool Artists",
        )
    }
)
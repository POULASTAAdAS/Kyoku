package com.poulastaa.view.presentation.others

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.view.presentation.components.ViewScreenWrapperCompact
import com.poulastaa.view.presentation.others.compoenets.ViewOtherTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ViewOtherCompactScreen(
    scroll: TopAppBarScrollBehavior,
    state: ViewOtherUiState,
    onAction: (ViewOtherUiAction) -> Unit,
    navigateBack: () -> Unit,
) {
    ViewScreenWrapperCompact(
        scroll = scroll,
        data = state.listOfSongs,
        name = state.root.title,
        cover = state.root.poster,
        totalSongs = state.listOfSongs.size,
        loadingType = state.loadingType,
        isTypeArtist = false,
        play = {
            onAction(ViewOtherUiAction.OnPlayAll(it))
        },
        onSongClick = { type, songId ->
            onAction(ViewOtherUiAction.OnSongClick(type, songId))
        },
        onSongThreeDotClick = {
            onAction(ViewOtherUiAction.OnSongMenuToggle(it))
        },
        topBar = {
            ViewOtherTopBar(
                scroll = scroll,
                title = state.root.title,
                type = state.type,
                navigateBack = navigateBack,
                onMenuClick = {
                    onAction(ViewOtherUiAction.OnMenuClick)
                }
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            ViewOtherCompactScreen(
                state = PREV_VIEW_DATA,
                scroll = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                onAction = {},
                navigateBack = {}
            )
        }
    }
}